package ru.stoker.database.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.usertype.UserType;
import ru.stoker.database.entity.productproperties.ProductProperties;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Objects;

public class ProductPropertiesJsonType implements UserType {

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public int[] sqlTypes() {
        return new int[]{Types.OTHER};
    }

    @Override
    public Class returnedClass() {
        return ProductProperties.class;
    }

    @Override
    public boolean equals(Object o, Object o1) throws HibernateException {
        return Objects.equals(o, o1);
    }

    @Override
    public int hashCode(Object o) throws HibernateException {
        return Objects.hash(o);
    }

    @Override
    public Object nullSafeGet(ResultSet resultSet, String[] strings, SharedSessionContractImplementor sharedSessionContractImplementor, Object o) throws HibernateException, SQLException {
        String value = resultSet.getString(strings[0]);
        if(value == null) {
            return null;
        }
        try {
            return mapper.readValue(value, ProductProperties.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void nullSafeSet(PreparedStatement preparedStatement, Object value, int i, SharedSessionContractImplementor sharedSessionContractImplementor) throws HibernateException, SQLException {
        if(value == null) {
            preparedStatement.setNull(i, Types.OTHER);
            return;
        }
        try {
            preparedStatement.setObject(i, mapper.writeValueAsString(value), Types.OTHER);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Object deepCopy(Object o) throws HibernateException {
        return o;
    }

    @Override
    public boolean isMutable() {
        return false;
    }

    @Override
    public Serializable disassemble(Object o) throws HibernateException {
        try {
            return mapper.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Object assemble(Serializable serializable, Object o) throws HibernateException {
        return mapper.convertValue(serializable, ProductProperties.class);
    }

    @Override
    public Object replace(Object o, Object o1, Object o2) throws HibernateException {
        return o;
    }
}
