package com.bitdubai.fermat_api.layer.osa_android.database_system;

import java.util.List;
import java.util.UUID;


import com.bitdubai.fermat_api.layer.all_definition.enums.interfaces.FermatEnum;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantDeleteRecordException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantInsertRecordException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantLoadTableToMemoryException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantUpdateRecordException;

/**
 *
 *  <p>The abstract class <code>DatabaseTable</code> is a interface
 *     that define the methods to manage a DatabaseTable object. Set filters and orders, and load records to memory.
 *
 *
 *  @author  Luis
 *  @version 1.0.0
 *  @since  01/01/15.
 * */
public interface DatabaseTable {
    
    List<DatabaseTableRecord> customQuery(String query, boolean customResult) throws CantLoadTableToMemoryException;

    DatabaseTableColumn newColumn();

    List<DatabaseTableRecord> getRecords();

    DatabaseTableRecord getEmptyRecord();

    void clearAllFilters();

    DatabaseTableFilter getEmptyTableFilter();

    DatabaseTableFilter getNewFilter(String column, DatabaseFilterType type, String value);

    DatabaseTableFilterGroup getNewFilterGroup(List<DatabaseTableFilter> tableFilters, List<DatabaseTableFilterGroup> filterGroups, DatabaseFilterOperator filterOperator);

    void updateRecord (DatabaseTableRecord record) throws CantUpdateRecordException;

    void insertRecord (DatabaseTableRecord record) throws CantInsertRecordException;

    void loadToMemory() throws CantLoadTableToMemoryException;

    boolean isTableExists();

    void addStringFilter(String columnName, String value, DatabaseFilterType type);

    void addFermatEnumFilter(String columnName, FermatEnum value, DatabaseFilterType type);

    void setFilterGroup(DatabaseTableFilterGroup filterGroup);

    void setFilterGroup(List<DatabaseTableFilter> tableFilters, List<DatabaseTableFilterGroup> filterGroups, DatabaseFilterOperator filterOperator);

    void addUUIDFilter(String columnName, UUID value, DatabaseFilterType type);

    void addFilterOrder(String columnName, DatabaseFilterOrder direction);

    void setFilterTop(String top);

    void setFilterOffSet(String offset);

    void addSelectOperator(String columnName, DataBaseSelectOperatorType operator, String alias);

    void deleteRecord(DatabaseTableRecord record) throws CantDeleteRecordException;

    @Deprecated // try to not use this when you're updating records. android database needs filters to update records.
    DatabaseTableRecord getRecordFromPk(String pk) throws Exception;

    // todo try to substract this method from here, they don't belong
    String makeFilter();
    String getTableName();
    List<DatabaseSelectOperator> getTableSelectOperator();

}
