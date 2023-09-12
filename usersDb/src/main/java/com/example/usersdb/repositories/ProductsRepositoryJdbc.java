package com.example.usersdb.repositories;

import com.example.usersdb.dto.ProductSpecDTO;
import com.example.usersdb.entities.Product;
import com.example.usersdb.entities.TagsForProducts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@Repository
public class ProductsRepositoryJdbc {
    private final JdbcTemplate jdbcTemplate;
    private static final String currentSchema = "users_schema";
    @Autowired
    public ProductsRepositoryJdbc(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Product> findBySpec(ProductSpecDTO productSpecDTO){
        String sql = "SELECT * FROM users_schema.products\n" +
                "WHERE";
        if (productSpecDTO.getName() != null && !productSpecDTO.getName().isEmpty())
            sql += " name = " + productSpecDTO.getName();
        if (productSpecDTO.getMinDate() != null)
            sql += " AND date >= " + productSpecDTO.getMinDate();
        if (productSpecDTO.getMaxDate() != null)
            sql += " AND date <= " + productSpecDTO.getMaxDate();
        if (productSpecDTO.getDescrContain() != null && !productSpecDTO.getDescrContain().isEmpty())
            sql += " AND description LIKE '%" + productSpecDTO.getDescrContain() + "%'";
//        if (productSpecDTO.getMinCost() != null)
//            spec = spec.and(ProductsSpecs.costGrThenOrEq(productSpecDTO.getMinCost()));
//        if (productSpecDTO.getMaxCost() != null)
//            spec = spec.and(ProductsSpecs.costLeThenOrEq(productSpecDTO.getMaxCost()));
//        if (productSpecDTO.getMinQuality() != null)
//            spec = spec.and(ProductsSpecs.qualityGrThenOrEq(productSpecDTO.getMinQuality()));
//        if (productSpecDTO.getMaxQuality() != null)
//            spec = spec.and(ProductsSpecs.qualityLeThenOrEq(productSpecDTO.getMaxQuality()));
//        if (productSpecDTO.getTagsIdList() != null && !productSpecDTO.getTagsIdList().isEmpty()) {
//            for (TagsForProducts tag : tagsForProductsRepository.findAllById(productSpecDTO.getTagsIdList())) {
//                spec = spec.or(ProductsSpecs.isContainTag(tag));
//            }
//        }
        return null;
    }

    public List<Product> findAll(){
        String sql = "SELECT * FROM " + currentSchema + ".products";
        return jdbcTemplate.query(sql, (resultSet, i) -> {
            Product p = new Product();
            p.setId(resultSet.getLong("id"));
            p.setName(resultSet.getString("name"));
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            try {
                p.setDate(dateFormat.parse(resultSet.getString("date")));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            p.setDescription(resultSet.getString("description"));
            p.setCost(Long.parseLong(resultSet.getString("cost")));
            p.setQuality(Long.parseLong(resultSet.getString("quality")));
            String sqlGetTags = "SELECT tags.tag_id as id, tags.name, tags.description, tags.usage FROM\n" +
                    "(SELECT products_tags.product_id as product_id, tags_for_products.id as tag_id, tags_for_products.name,\n" +
                    "        tags_for_products.description,\n" +
                    "        tags_for_products.usage FROM users_schema.products_tags\n" +
                    "         JOIN users_schema.tags_for_products ON tags_for_products.id = products_tags.tag_id) as \"tags\"\n" +
                    "WHERE product_id = ?";
            for(Map<String, Object> m : jdbcTemplate.queryForList(sqlGetTags, p.getId())){
                TagsForProducts tag = new TagsForProducts();
                tag.setId((long) m.get("id"));
                tag.setUsage((long) m.get("usage"));
                tag.setName((String) m.get("name"));
                tag.setDescription((String) m.get("description"));
                p.getTags().add(tag);
            }
            return p;
        });
    }
}
