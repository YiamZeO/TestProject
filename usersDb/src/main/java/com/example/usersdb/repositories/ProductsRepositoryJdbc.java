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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

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
                SET name = :name, date = :date, descripion = :description,
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
        String sql = "SELECT * FROM users_schema.products WHERE";
        Map<String, Object> params = new HashMap<>();
        if (productSpecDTO.getName() != null && !productSpecDTO.getName().isEmpty()) {
            sql += " name = :productName AND";
            params.put("productName", productSpecDTO.getName());
        }
        if (productSpecDTO.getMinDate() != null) {
            sql += " date >= :productMinDate AND";
            params.put("productMinDate", productSpecDTO.getMinDate());
        }
        if (productSpecDTO.getMaxDate() != null) {
            sql += " date <= :productMaxDate AND";
            params.put("productMaxDate", productSpecDTO.getMaxDate());
        }
        if (productSpecDTO.getDescrContain() != null && !productSpecDTO.getDescrContain().isEmpty()) {
            sql += " description LIKE '%:productDescrContain%' AND";
            params.put("productDescrContain", productSpecDTO.getDescrContain());
        }
        if (productSpecDTO.getMinCost() != null) {
            sql += " cost >= :productMinCost AND";
            params.put("productMinCost", productSpecDTO.getMinCost());
        }
        if (productSpecDTO.getMaxCost() != null) {
            sql += " cost <= :productMaxCost AND";
            params.put("productMaxCost", productSpecDTO.getMaxCost());
        }
        if (productSpecDTO.getMinQuality() != null) {
            sql += " quality >= :productMinQuality AND";
            params.put("productMinQuality", productSpecDTO.getMinQuality());
        }
        if (productSpecDTO.getMaxQuality() != null) {
            sql += " quality <= :productMaxQuality AND";
            params.put("productMaxQuality", productSpecDTO.getMaxQuality());
        }
        List<Product> products;
        sql += " TRUE";
        if (!params.isEmpty()) {
            products = namedJdbcTemplate.query(sql, params,
                    (resultSetProducts, iProduct) -> productMapper(resultSetProducts));
        } else {
            products = namedJdbcTemplate.query(sql,
                    (resultSetProducts, iProduct) -> productMapper(resultSetProducts));
        }
        for (Product p : products) {
            p.setTags(new HashSet<>(findTagsForProduct(p.getId())));
        }
        return products;
    }

    public List<TagsForProducts> findTagsForProduct(Long productId) {
        String sqlGetTags = """
                SELECT tags.tag_id as id, tags.name, tags.description, tags.usage FROM
                (SELECT products_tags.product_id as product_id, tags_for_products.id as tag_id, tags_for_products.name,
                        tags_for_products.description,
                        tags_for_products.usage FROM users_schema.products_tags
                         JOIN users_schema.tags_for_products ON tags_for_products.id = products_tags.tag_id) as "tags"
                WHERE product_id = :productId""";
        Map<String, Object> params = new HashMap<>();
        params.put("productId", productId);
        return namedJdbcTemplate.query(sqlGetTags, params, (resultSetTags, iTag) -> {
            TagsForProducts tag = new TagsForProducts();
            tag.setId(resultSetTags.getLong("id"));
            tag.setUsage(resultSetTags.getLong("usage"));
            tag.setName(resultSetTags.getString("name"));
            tag.setDescription(resultSetTags.getString("description"));
            return tag;
        });
    }
}
