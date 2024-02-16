package com.example.demo.repository.implementation;

import com.example.demo.exception.ContactNotFoundException;
import com.example.demo.model.Contact;
import com.example.demo.repository.ContactRepository;
import com.example.demo.repository.mapper.ContactRowMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.ArgumentPreparedStatementSetter;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapperResultSetExtractor;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Primary
@RequiredArgsConstructor
@Repository
public class ContactDBRepository implements ContactRepository {

    private final JdbcTemplate template;

    @Override
    public List<Contact> findAll() {

        String sql = "SELECT * FROM contacts";

        return template.query(sql, new ContactRowMapper());
    }

    @Override
    public Optional<Contact> findById(Long id) {

        String sql = "SELECT * FROM contacts WHERE id = ?";
        Contact contact = DataAccessUtils.singleResult(
                template.query(
                        sql,
                        new ArgumentPreparedStatementSetter(new Object[] {id}),
                                new RowMapperResultSetExtractor<>(new ContactRowMapper(), 1))
        );

        return Optional.ofNullable(contact);
    }

    @Override
    public Contact save(Contact contact) {

        contact.setId(System.currentTimeMillis());
        String sql = "INSERT INTO contacts (firstName, lastName, email, phone, id) VALUES (?, ?, ?, ?, ?)";
        template.update(sql, contact.getFirstName(), contact.getLastName(), contact.getEmail(),
                contact.getPhone(), contact.getId());

        return contact;
    }

    @Override
    public Contact update(Contact contact) {

        Contact contactToUpdate = findById(contact.getId()).orElse(null);

        if(contactToUpdate != null){
            String sql = "UPDATE contacts SET firstName = ?, lastName = ?, email = ?, phone = ? WHERE id = ?";
            template.update(sql, contact.getFirstName(), contact.getLastName(), contact.getEmail(),
                    contact.getPhone(), contact.getId());

            return contact;
        }

        throw new ContactNotFoundException("Contact for update not found. ID: " + contact.getId());
    }

    @Override
    public void deleteById(Long id) {

        String sql = "DELETE FROM contacts WHERE id = ?";
        template.update(sql, id);

    }

    @Override
    public void batchInsert(List<Contact> contactList) {

        String sql = "INSERT INTO contacts (firstName, lastName, email, phone, id) VALUES (?, ?, ?, ?, ?)";
        template.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                Contact contact = contactList.get(i);
                ps.setString(1, contact.getFirstName());
                ps.setString(2, contact.getLastName());
                ps.setString(3, contact.getEmail());
                ps.setString(4, contact.getPhone());
                ps.setLong(5, contact.getId());
            }

            @Override
            public int getBatchSize() {
                return contactList.size();
            }
        });

    }
}
