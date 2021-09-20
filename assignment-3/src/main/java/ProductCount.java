class ProductCount implements Comparable<ProductCount>{
    Product product;
    int count = 0;

    ProductCount(Product product) {
        this.product = product;
    }


    public Product getProduct() {
        return product;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public int compareTo(ProductCount o) {
        return this.getCount() - o.getCount();
    }
}