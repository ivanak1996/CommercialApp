package com.example.commercialapp.fragments;

import android.os.*;
import android.view.*;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.fragment.app.*;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.*;

import com.example.commercialapp.JsonParser;
import com.example.commercialapp.asyncResponses.*;
import com.example.commercialapp.R;
import com.example.commercialapp.adapters.ProductAdapter;
import com.example.commercialapp.dialogs.ProductDialogFragment;
import com.example.commercialapp.models.*;
import com.example.commercialapp.roomDatabase.products.Product;
import com.example.commercialapp.roomDatabase.products.ProductViewModel;
import com.example.commercialapp.roomDatabase.user.*;

import java.util.*;

public class ProductListFragment extends Fragment implements ProductListAsyncResponse, GetUserFromDbAsyncResponse, ProductGroupsAsyncResponse {

    private RecyclerView productListRecyclerView;
    private ProductAdapter productListAdapter;
    private List<Product> resultProductList = new ArrayList<>();

    private Spinner productGroupSpinner;
    private List<ProductGroupModel> productGroupModels = new ArrayList<>();

    private LinearLayout loading;
    private LinearLayout loaded;

    private UserViewModel userViewModel;
    private User user;

    private ProductViewModel productViewModel;
    private List<Product> savedProductsList = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_list, container, false);

        setRetainInstance(true);

        // product list setup
        productListRecyclerView = view.findViewById(R.id.recycler_view_productsList);
        productListRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        productListRecyclerView.setHasFixedSize(true);

        productListAdapter = new ProductAdapter();
        productListAdapter.setOnItemClickListener(new ProductAdapter.ProductAdapterItemClickListener() {
            @Override
            public void onAddItemClick(int position) {
                Product product = productListAdapter.getProduct(position);
                DialogFragment newFragment = new ProductDialogFragment(ProductListFragment.this, product);
                newFragment.show(getActivity().getSupportFragmentManager(), "missiles");
            }
        });

        productListRecyclerView.setAdapter(productListAdapter);

        // get user data from database
        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        userViewModel.getUser(this);

        // get product information from db
        productViewModel = ViewModelProviders.of(this).get(ProductViewModel.class);
        productViewModel.getAllProductsInOpenedOrder().observe(ProductListFragment.this.getActivity(), new Observer<List<Product>>() {
            @Override
            public void onChanged(List<Product> products) {
                savedProductsList = products;
                productListAdapter.notifyDataSetChanged();
            }
        });

        // search bar
        SearchView searchView = view.findViewById(R.id.search_view_products);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                new GetProductListFromApiAsyncTask(ProductListFragment.this, user.getEmail(), user.getPassword(), query).execute();
                getActivity().getCurrentFocus().clearFocus();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        // spinner for product group dropdown menu
        productGroupSpinner = view.findViewById(R.id.spinner_productType);
        productGroupSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedGroup = productGroupModels.get(position).getA();
                productListAdapter.setProducts(filterListBySelectedGroup(selectedGroup));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // preview loading screen while user data from db is being awaited
        loading = view.findViewById(R.id.product_list_loading);
        loaded = view.findViewById(R.id.product_list_loading);
        loading.setVisibility(View.VISIBLE);
        loaded.setVisibility(View.GONE);

        return view;
    }


    // search finished
    @Override
    public void processFinish(List<Product> models) {
        resultProductList = setResults(models);
        String selectedGroup = productGroupModels.get(productGroupSpinner.getSelectedItemPosition()).getA();
        productListAdapter.setProducts(filterListBySelectedGroup(selectedGroup));
    }

    // user has been loaded from db
    @Override
    public void processFinish(User output) {
        user = output;
        new GetProductGroupListFromApiAsyncTask(this, user.getEmail(), user.getPassword()).execute();
        loaded.setVisibility(View.VISIBLE);
        loading.setVisibility(View.GONE);
    }

    // product groups have been fetched from api
    @Override
    public void productGroupsProcessFinish(List<ProductGroupModel> productGroups) {
        productGroupModels = productGroups;
        ArrayAdapter<ProductGroupModel> spinnerArrayAdapter = new ArrayAdapter<>
                (getContext(), android.R.layout.simple_spinner_item, productGroups); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        productGroupSpinner.setAdapter(spinnerArrayAdapter);
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
                filteredProducts.add(product);
            }
        }

        return filteredProducts;
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
