package com.example.usersdb.repositories;

import com.example.usersdb.dto.ProductSpecDTO;
import com.example.usersdb.entities.Product;
import com.example.usersdb.entities.TagsForProducts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Repository
public class ProductsRepositoryJdbc {
    private final NamedParameterJdbcTemplate namedJdbcTemplate;

    @Autowired
    public ProductsRepositoryJdbc(NamedParameterJdbcTemplate jdbcTemplate) {
        this.namedJdbcTemplate = jdbcTemplate;
    }

    public Product productMapper(ResultSet resultSetProduct) throws SQLException {
        Product p = new Product();
        p.setId(resultSetProduct.getLong("id"));
        p.setName(resultSetProduct.getString("name"));
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            p.setDate(dateFormat.parse(resultSetProduct.getString("date")));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        p.setDescription(resultSetProduct.getString("description"));
        p.setCost(Long.parseLong(resultSetProduct.getString("cost")));
        p.setQuality(Long.parseLong(resultSetProduct.getString("quality")));
        return p;
    }

    public void productAddTag(Long productId, Long tagId) {
        String sql = "INSERT INTO users_schema.products_tags\n" +
                "VALUES (:prId, :tgId)";
        Map<String, Object> params = new HashMap<>();
        params.put("prId", productId);
        params.put("tgId", tagId);
        namedJdbcTemplate.update(sql, params);
    }

    public void productDelTag(Long productId, Long tagId) {
        String sql = "DELETE FROM users_schema.products_tags\n" +
                "WHERE product_id = :prId AND tag_id = :tgId";
        Map<String, Object> params = new HashMap<>();
        params.put("prId", productId);
        params.put("tgId", tagId);
        namedJdbcTemplate.update(sql, params);
    }

    public Product findById(Long productId) {
        String sql = "SELECT * FROM users_schema.products WHERE products.id = :productId";
        Map<String, Object> params = new HashMap<>();
        params.put("productId", productId);
        return namedJdbcTemplate.query(sql, params, this::productMapper);
    }

    public void deleteById(Long productId) {
        String sql = "DELETE FROM users_schema.products\n" +
                "WHERE id = :id";
        Map<String, Object> params = new HashMap<>();
        params.put("id", productId);
        namedJdbcTemplate.update(sql, params);
    }

    public void updateProduct(Product product, Long productId) {
        String sql = """
                UPDATE users_schema.products
                SET name = :name, date = :date, description = :description,
                cost = :cost, quality = :quality WHERE id = :id""";
        Map<String, Object> params = new HashMap<>();
        params.put("name", product.getName());
        params.put("date", product.getDate());
        params.put("description", product.getDescription());
        params.put("cost", product.getCost());
        params.put("quality", product.getQuality());
        params.put("id", productId);
        namedJdbcTemplate.update(sql, params);
    }

    public void insertProduct(Product product) {
        String sql = """
                INSERT INTO users_schema.products (name, date, description, cost, quality)
                VALUES (:name, :date, :description, :cost, :quality)
                """;
        Map<String, Object> params = new HashMap<>();
        params.put("name", product.getName());
        params.put("date", product.getDate());
        params.put("description", product.getDescription());
        params.put("cost", product.getCost());
        params.put("quality", product.getQuality());
        namedJdbcTemplate.update(sql, params);
    }

    public List<Product> findBySpec(ProductSpecDTO productSpecDTO) {
        StringBuilder sql = new StringBuilder("""
                SELECT * FROM users_schema.products 
                JOIN users_schema.products_tags ON products.id = products_tags.product_id
                 WHERE 
                """);
        Map<String, Object> params = new HashMap<>();
        if (productSpecDTO.getName() != null && !productSpecDTO.getName().isEmpty()) {
            sql.append(" products.name = :productName AND");
            params.put("productName", productSpecDTO.getName());
        }
        if (productSpecDTO.getMinDate() != null) {
            sql.append(" products.date >= :productMinDate AND");
            params.put("productMinDate", productSpecDTO.getMinDate());
        }
        if (productSpecDTO.getMaxDate() != null) {
            sql.append(" products.date <= :productMaxDate AND");
            params.put("productMaxDate", productSpecDTO.getMaxDate());
        }
        if (productSpecDTO.getDescrContain() != null && !productSpecDTO.getDescrContain().isEmpty()) {
            sql.append(" products.description LIKE '%:productDescrContain%' AND");
            params.put("productDescrContain", productSpecDTO.getDescrContain());
        }
        if (productSpecDTO.getMinCost() != null) {
            sql.append(" products.cost >= :productMinCost AND");
            params.put("productMinCost", productSpecDTO.getMinCost());
        }
        if (productSpecDTO.getMaxCost() != null) {
            sql.append(" products.cost <= :productMaxCost AND");
            params.put("productMaxCost", productSpecDTO.getMaxCost());
        }
        if (productSpecDTO.getMinQuality() != null) {
            sql.append(" products.quality >= :productMinQuality AND");
            params.put("productMinQuality", productSpecDTO.getMinQuality());
        }
        if (productSpecDTO.getMaxQuality() != null) {
            sql.append(" products.quality <= :productMaxQuality AND");
            params.put("productMaxQuality", productSpecDTO.getMaxQuality());
        }
        if(productSpecDTO.getTagsIdList() != null && !productSpecDTO.getTagsIdList().isEmpty()){
            sql.append(" (");
            for (int i = 0; i < productSpecDTO.getTagsIdList().size(); i++){
                sql.append(" products_tags.tag_id = :tagId").append(i)
                        .append(" OR");
                params.put("tagId" + i, productSpecDTO.getTagsIdList().get(i));
            }
            sql.append(" FALSE) AND");
        }
        sql.append(" TRUE");
        List<Product> products;
        if (!params.isEmpty()) products = namedJdbcTemplate.query(sql.toString(), params,
                (resultSetProducts, iProduct) -> productMapper(resultSetProducts));
        else products = namedJdbcTemplate.query(sql.toString(),
                (resultSetProducts, iProduct) -> productMapper(resultSetProducts));
        if(!products.isEmpty()){
            findTagsForProducts(products);
        }
        return products;
    }

    public void findTagsForProducts(List<Product> products) {
        String sqlGetTags = """
                SELECT products_tags.product_id, tags_for_products.id, tags_for_products.name,
                        tags_for_products.description,
                        tags_for_products.usage FROM users_schema.products_tags
                         JOIN users_schema.tags_for_products ON tags_for_products.id = products_tags.tag_id
                WHERE product_id IN (:productIds)""";
        Map<String, Object> params = new HashMap<>();
        List<Long> productIds = new ArrayList<>();
        for (Product p : products)
            productIds.add(p.getId());
        params.put("productIds", productIds);
        namedJdbcTemplate.query(sqlGetTags, params, (resultSetTags, iTag) -> {
            TagsForProducts tag = new TagsForProducts();
            tag.setId(resultSetTags.getLong("id"));
            tag.setUsage(resultSetTags.getLong("usage"));
            tag.setName(resultSetTags.getString("name"));
            tag.setDescription(resultSetTags.getString("description"));
            Optional<Product> o = products.stream().filter(p -> {
                try {
                    return p.getId() == resultSetTags.getLong("product_id");
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }).findFirst();
            o.ifPresent(product -> product.getTags().add(tag));
            return null;
        });
    }
}
