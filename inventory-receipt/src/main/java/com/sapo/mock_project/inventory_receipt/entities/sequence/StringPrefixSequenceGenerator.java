package com.sapo.mock_project.inventory_receipt.entities.sequence;

import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.enhanced.SequenceStyleGenerator;
import org.hibernate.internal.util.config.ConfigurationHelper;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.Type;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class StringPrefixSequenceGenerator extends SequenceStyleGenerator {
    public static final String VALUE_PREFIX_PARAMETER = "valuePrefix";
    public static final String VALUE_PREFIX_DEFAULT = "";
    private String valuePrefix;

    public static final String NUMBER_FORMAT_PARAMETER = "numberFormat";
    public static final String NUMBER_FORMAT_DEFAULT = "%d";
    private String numberFormat;

    public static final String SEQUENCE_TABLE_PARAMETER = "sequenceTable";
    public static final String SEQUENCE_TABLE_DEFAULT = "";
    private String sequenceTable;

    @Override
    public void configure(Type type, Properties params, ServiceRegistry serviceRegistry) throws MappingException {
        super.configure(type, params, serviceRegistry);
        valuePrefix = ConfigurationHelper.getString(VALUE_PREFIX_PARAMETER, params, VALUE_PREFIX_DEFAULT);
        numberFormat = ConfigurationHelper.getString(NUMBER_FORMAT_PARAMETER, params, NUMBER_FORMAT_DEFAULT);
        sequenceTable = ConfigurationHelper.getString(SEQUENCE_TABLE_PARAMETER, params, SEQUENCE_TABLE_DEFAULT);
    }

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
        String query = "INSERT INTO " + sequenceTable + " VALUES (NULL)";
        try (Statement statement = session.getJdbcConnectionAccess().obtainConnection().createStatement()) {
            statement.executeUpdate(query, Statement.RETURN_GENERATED_KEYS);
            ResultSet rs = statement.getGeneratedKeys();
            if (rs.next()) {
                long seqValue = rs.getLong(1);
                return valuePrefix + String.format(numberFormat, seqValue);
            }
        } catch (SQLException e) {
            throw new HibernateException("Unable to generate Sequence", e);
        }
        return null;
    }
}
