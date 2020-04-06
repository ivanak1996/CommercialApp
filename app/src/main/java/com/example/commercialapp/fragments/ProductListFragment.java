package com.example.commercialapp.fragments;

import android.os.*;
import android.view.*;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.*;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.*;

import com.example.commercialapp.JsonParser;
import com.example.commercialapp.ProductListActivity;
import com.example.commercialapp.asyncResponses.*;
import com.example.commercialapp.R;
import com.example.commercialapp.adapters.ProductAdapter;
import com.example.commercialapp.models.*;
import com.example.commercialapp.roomDatabase.orders.Order;
import com.example.commercialapp.roomDatabase.orders.OrderViewModel;
import com.example.commercialapp.roomDatabase.products.Product;
import com.example.commercialapp.roomDatabase.products.ProductViewModel;
import com.example.commercialapp.roomDatabase.user.*;

import java.util.*;

public class ProductListFragment extends Fragment implements ProductListAsyncResponse, ProductGroupsAsyncResponse, GetOpenedOrderAsyncResponse, InsertOrderAsyncResponse {

    private RecyclerView productListRecyclerView;
    private ProductAdapter productListAdapter;
    private List<Product> resultProductList = new ArrayList<>();

    private Spinner spinner;
    private List<ProductGroupModel> productGroupModels = new ArrayList<>();

    private LinearLayout loading;
    private LinearLayout loaded;
    private LinearLayout productListWrapper;

    private User user;

    private ProductViewModel productViewModel;
    private List<Product> savedProductsList = new ArrayList<>();

    private OrderViewModel orderViewModel;
    private long openedOrderId;

    private TextView noDataInRecyclerView;
    private TextView productCountTextView;

    public ProductListFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.chart_menu, menu);

        MenuItem spinnerItem = menu.findItem(R.id.spinner);
        spinner = (Spinner) spinnerItem.getActionView();
        new GetProductGroupListFromApiAsyncTask(this, user.getEmail(), user.getPassword()).execute();
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedGroup = productGroupModels.get(position).getA();
                productListAdapter.setProducts(filterListBySelectedGroup(selectedGroup));
                productListAdapter.notifyDataSetChanged();
                refreshRecyclerViewAppearance();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        MenuItem cartItem = menu.findItem(R.id.action_chart);
        cartItem.setActionView(R.layout.layout_menu_chart);
        View av = cartItem.getActionView();
        productCountTextView = av.findViewById(R.id.actionbar_count);
        if (savedProductsList.size() > 0)
            productCountTextView.setText("" + savedProductsList.size());
        av.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(getActivity(), R.id.nav_host_fragment)
                        .navigate(R.id.anotherFragment);
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_list, container, false);

        setRetainInstance(true);

        noDataInRecyclerView = view.findViewById(R.id.empty_search_results);

        // product list setup
        productListWrapper = view.findViewById(R.id.layout_productList);

        productListRecyclerView = view.findViewById(R.id.recycler_view_productsList);
        productListRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        productListRecyclerView.setHasFixedSize(true);

        productListAdapter = new ProductAdapter();
        productListAdapter.setOnItemClickListener(new ProductAdapter.ProductAdapterItemClickListener() {
            @Override
            public void onClick(int position) {
                productListAdapter.chooseProduct(position, productViewModel, openedOrderId);
            }
        });

        productListRecyclerView.setAdapter(productListAdapter);

        this.user = ((ProductListActivity) getActivity()).getUser();

        // get product information from db
        productViewModel = ViewModelProviders.of(this).get(ProductViewModel.class);
        productViewModel.getAllProductsInOpenedOrder().observe(ProductListFragment.this.getActivity(), new Observer<List<Product>>() {
            @Override
            public void onChanged(List<Product> products) {
                savedProductsList = products;
                if (productCountTextView != null) {
                    int numberOfSavedProducts = savedProductsList.size();
                    if (numberOfSavedProducts > 0) {
                        productCountTextView.setText("" + numberOfSavedProducts);
                    } else {
                        productCountTextView.setText("");
                    }
                }
                resultProductList = setResults(resultProductList);
                productListAdapter.notifyDataSetChanged();
            }
        });

        // get opened order into which the user shall add the items
        orderViewModel = ViewModelProviders.of(this).get(OrderViewModel.class);
        orderViewModel.getOpenedOrder(this);

        // search bar
        final SearchView searchView = view.findViewById(R.id.search_view_products);
        ImageView searchIcon = view.findViewById(R.id.imageView_search);
        searchIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String query = searchView.getQuery().toString();
                setStateLoading();
                new GetProductListFromApiAsyncTask(ProductListFragment.this, user.getEmail(), user.getPassword(), query).execute();
            }
        });


        // preview loading screen while user data from db is being awaited
        loading = view.findViewById(R.id.product_list_loading);
        loaded = view.findViewById(R.id.product_list_loaded);
        loaded.setVisibility(View.VISIBLE);
        loading.setVisibility(View.GONE);

        return view;
    }

    private void setStateLoading() {
        loaded.setVisibility(View.GONE);
        loading.setVisibility(View.VISIBLE);
    }

    private void setStateLoaded() {
        loaded.setVisibility(View.VISIBLE);
        loading.setVisibility(View.GONE);
    }

    @Override
    public void onStop() {
        super.onStop();
        productListAdapter.saveProductState(productViewModel, openedOrderId);
    }

    // search finished
    @Override
    public void processFinish(List<Product> models) {
        resultProductList = setResults(models);
        String selectedGroup = productGroupModels.get(spinner.getSelectedItemPosition()).getA();
        productListAdapter.setProducts(filterListBySelectedGroup(selectedGroup));
        productListAdapter.notifyDataSetChanged();
        setStateLoaded();
        refreshRecyclerViewAppearance();
    }

    // product groups have been fetched from api
    @Override
    public void productGroupsProcessFinish(List<ProductGroupModel> productGroups) {
        productGroupModels = productGroups;
        ArrayAdapter<ProductGroupModel> spinnerArrayAdapter = new ArrayAdapter<>
                (getContext(), android.R.layout.simple_spinner_item, productGroups); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerArrayAdapter);
    }

    private List<Product> filterListBySelectedGroup(String selectedGroup) {

        if (selectedGroup.isEmpty())
            return ProductListFragment.this.resultProductList;

        List<Product> filteredProducts = new ArrayList<>();
        for (Product product : ProductListFragment.this.resultProductList) {
            if (selectedGroup.equals(product.getD())) {
                filteredProducts.add(product);
            }
        }
        return filteredProducts;
    }

    // method that merges the non-db saved and saved product
    private List<Product> setResults(List<Product> results) {

        List<Product> filteredProducts = new ArrayList<>();

        for (Product product : results) {
            boolean savedProductFound = false;
            for (Product savedProduct : savedProductsList) {
                if (savedProduct.getB().equals(product.getB())) {
                    filteredProducts.add(savedProduct);
                    savedProductFound = true;
                    break;
                }
            }
            if (savedProductFound == false) {
                product.setQuantity(0);
                filteredProducts.add(product);
            }
        }

        return filteredProducts;
    }

    private void refreshRecyclerViewAppearance() {
        if (productListAdapter.getItemCount() == 0) {
            productListWrapper.setVisibility(View.GONE);
            noDataInRecyclerView.setVisibility(View.VISIBLE);
        } else {
            productListWrapper.setVisibility(View.VISIBLE);
            noDataInRecyclerView.setVisibility(View.GONE);
        }
    }

    @Override
    public void getOpenedOrderFinish(Order resultOrder) {
        Order order = resultOrder;
        if (order == null) {
            order = new Order(Order.STATUS_OPEN, new Date(System.currentTimeMillis()));
            orderViewModel.insert(order, this);
        } else {
            openedOrderId = resultOrder.getRowId();
        }
    }

    @Override
    public void insertOrderFinish(long id) {
        this.openedOrderId = id;
    }

    private static class GetProductListFromApiAsyncTask extends AsyncTask<Void, Void, List<Product>> {

        private ProductListAsyncResponse delegate;
        private String username;
        private String password;
        private String keyword;

        public GetProductListFromApiAsyncTask(ProductListAsyncResponse delegate, String username, String password, String keyword) {
            this.delegate = delegate;
            this.username = username;
            this.password = password;
            this.keyword = keyword;
        }

        @Override
        protected List<Product> doInBackground(Void... voids) {
            return JsonParser.getProductsFromApi(username, password, keyword);
        }

        @Override
        protected void onPostExecute(List<Product> productModels) {
            delegate.processFinish(productModels);
        }
    }

    private static class GetProductGroupListFromApiAsyncTask extends AsyncTask<Void, Void, List<ProductGroupModel>> {

        private ProductGroupsAsyncResponse delegate;
        private String username;
        private String password;

        public GetProductGroupListFromApiAsyncTask(ProductGroupsAsyncResponse delegate, String username, String password) {
            this.delegate = delegate;
            this.username = username;
            this.password = password;
        }

        @Override
        protected List<ProductGroupModel> doInBackground(Void... voids) {
            return JsonParser.getProductGroupsFromApi(username, password);
        }

        @Override
        protected void onPostExecute(List<ProductGroupModel> productGroupModels) {
            delegate.productGroupsProcessFinish(productGroupModels);
        }
    }
}
