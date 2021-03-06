package com.codecool.shop.dao.implementation.postgresql;

import com.codecool.shop.dao.CartDao;
import com.codecool.shop.model.Cart;
import com.codecool.shop.model.CartItem;
import com.codecool.shop.model.Product;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

import com.codecool.shop.dao.implementation.postgresql.query_util.CartQueryHandler;
import com.codecool.shop.util.CartStatusType;

public class CartDaoSql extends CartQueryHandler implements CartDao {

    private static CartDaoSql singletonInstance = null;

    private CartDaoSql() {
    }

    public static CartDaoSql getInstance() {
        if (singletonInstance == null) {
            singletonInstance = new CartDaoSql();
        }
        return singletonInstance;
    }

    @Override
    public int generateIdForNewCart() {
        return getLargestCartId() + 1;
    }

    /**
     * Initializes empty Cart with an ID.
     * */
    @Override
    public void add(Cart cart) {

        insertInto_order_And_order_product(cart);

    }

    @Override
    public Cart find(int id) {

        String query = "SELECT\n" +
                "  \"order\".id AS id_from_order,\n" +
                "  \"order\".user_id AS id_from_user,\n" +
                "  \"order\".status,\n" +
                "  p.id AS id_from_product,\n" +
                "  p.name,\n" +
                "  p.default_price,\n" +
                "  op.product_quantity\n" +
                "FROM \"order\"\n" +
                "  FULL OUTER JOIN order_product op on \"order\".id = op.order_id\n" +
                "  FULL OUTER JOIN product p on op.product_id = p.id\n" +
                "WHERE \"order\".id = '" + id + "'\n" +
                "ORDER BY id_from_order;";

        return getCartByOrder(query);
    }

    @Override
    public void remove(int id) {
        String orderProductQuery = "DELETE FROM order_product WHERE order_id ='" + id + "';";
        boolean deletionFromOrderProductSuccessful = DMLexecute(orderProductQuery);

        String orderQuery = "DELETE FROM order WHERE id ='" + id + "';";
        boolean deletionFromOrderSuccessful = DMLexecute(orderQuery);

        if (!(deletionFromOrderProductSuccessful && deletionFromOrderSuccessful)) {
            System.out.println("Unsuccessful deletion of cart with id: " + id);
        }
    }

    @Override
    public void remove(CartStatusType status) {
        String orderProductQuery = "DELETE FROM public.\"order\" WHERE status = '" + status.getStatusString() + "';";
        boolean deletionFromOrderProductSuccessful = DMLexecute(orderProductQuery);

        if (!deletionFromOrderProductSuccessful) {
            System.out.println("No cart was deleted");
        }

    }

    @Override
    public List<Cart> getAll() {
        return getAllCarts();
    }

    @Override
    public Cart getLastCart() {
        Cart lastCart;

        if (getLargestCartId() == 0) {
            lastCart = new Cart(1);
        } else {
            lastCart = find(getLargestCartId());
        }

        return lastCart;
    }

    @Override
    public void addToLastCart(Product newProduct) {
        addProductToCartBy(getLargestCartId(), newProduct);
    }

    @Override
    public void addProductToCartBy(int cartId, Product newProduct) {
        int newProductId = newProduct.getId();
        boolean productNotInCart = true;
        for (CartItem cartItem: find(cartId).getCartItemList()) {
            int existingCartItemProductId = cartItem.getProduct().getId();
            if (newProductId == existingCartItemProductId) {
                updateQuantityIn_order_product(
                        cartId, existingCartItemProductId, (cartItem.getQuantity() + 1)
                );
                productNotInCart = false;
                break;
            }
        }
        if (productNotInCart) {
            addNewProductToCartBy(cartId, newProductId);
        }
    }


    @Override
    public int getQuantityOfProductsInLastCart() {
        List<CartItem> lastCartItemList = getLastCart().getCartItemList();
        int numberOfProducts = 0;
        for (CartItem cartItem : lastCartItemList) {
            numberOfProducts += cartItem.getQuantity();
        }
        return numberOfProducts;
    }

    @Override
    public void saveChangesInCartAutomatically(List<CartItem> updatedCartsItemList) {

        List<CartItem> obsoleteCartsItemList = getLastCart().getCartItemList();
        for (CartItem obsoleteCartItem : obsoleteCartsItemList) {
            int obsoleteProductId = obsoleteCartItem.getProduct().getId();
            int obsoleteProductQuantity = obsoleteCartItem.getQuantity();

            boolean updatedCartItemDeleted = true;
            for (CartItem updatedCartItem: updatedCartsItemList) {

                int updatedProductId = updatedCartItem.getProduct().getId();
                int updatedProductQuantity = updatedCartItem.getQuantity();

                if (obsoleteProductId == updatedProductId) {
                    updatedCartItemDeleted = false;
                    if (updatedProductQuantity != obsoleteProductQuantity) {
                        updateQuantityIn_order_product(
                                getLargestCartId(), updatedProductId, updatedProductQuantity
                        );
                        break;
                    }
                }
            }

            if (updatedCartItemDeleted) {
                deleteCartItemFromCart(getLargestCartId(), obsoleteProductId);
            }
        }

        updateTotalPriceOfCartBy(getLargestCartId());

    }

    private void updateTotalPriceOfCartBy(int cartId) {
        BigDecimal totalPriceValue = getTotalPriceOfLastCart();

        String query = "UPDATE \"order\"\n" +
                " SET total_price = " + totalPriceValue + "\n" +
                " WHERE id = " + cartId + ";";

        DMLexecute(query);

    }

    @Override
    public BigDecimal getSubTotalPriceFromLastCartBy(int productId) {
        BigDecimal defaultPrice = getDefaultPriceFromLastCartBy(productId);
        BigDecimal quantity = getQuantityFromLastCartBy(productId);

        BigDecimal bigDecimalSubtotal = defaultPrice.multiply(quantity);

        return bigDecimalSubtotal.setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public BigDecimal getDefaultPriceFromLastCartBy(int productId) {

        String columnName = "default_price";

        String query = "SELECT " + columnName + "\n" +
                " FROM product\n" +
                " WHERE id = " + productId + ";";

        BigDecimal possiblyDefaultPrice = executeQueryWithColumnLabel_ReturnBigDecimal(query, columnName);

        if (possiblyDefaultPrice == null) {
            throw new NullPointerException();
        }

        return possiblyDefaultPrice.setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public BigDecimal getQuantityFromLastCartBy(int productId) {
        String columnName = "product_quantity";

        String query = "SELECT\n" +
                "  op.product_quantity\n" +
                " FROM product AS p\n" +
                "  FULL OUTER JOIN order_product op on p.id = op.product_id\n" +
                " WHERE p.id = " + productId + " AND op.order_id = " + getLargestCartId() + ";";

        BigDecimal possiblyProductQuantity = executeQueryWithColumnLabel_ReturnBigDecimal(query, columnName);

        if (possiblyProductQuantity == null) {
            throw new NullPointerException();
        }

        return possiblyProductQuantity.setScale(2, RoundingMode.HALF_UP);

    }

    @Override
    public Currency getCurrencyFromLastCartBy(int productId) throws NullPointerException {

        String columnName = "default_currency";

        String query = "SELECT " + columnName + "\n" +
                " FROM product\n" +
                " WHERE id = " + productId + ";";

        String possiblyCurrency = executeQueryWithColumnLabelById_ReturnString(query, columnName);

        if (possiblyCurrency == null) {
            throw new NullPointerException("There is no product with given ID: " + productId);
        }

        return Currency.getInstance(possiblyCurrency);
    }

    @Override
    public void updateLastCartStatus(CartStatusType status) {
        String updateStatus = "UPDATE \"order\"\n" +
                "SET status = '" + status + "'\n" +
                "WHERE id = " + getLargestCartId() + ";";

        DMLexecute(updateStatus);

    }

    @Override
    public void updateCartStatusBy(int cartId, CartStatusType status) {
        String updateStatus = "UPDATE \"order\"\n" +
                "SET status = '" + status + "'\n" +
                "WHERE id = " + cartId + ";";

        DMLexecute(updateStatus);
    }
}
