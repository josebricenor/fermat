package com.bitdubai.fermat_dap_plugin.layer.wallet.wallet.redeem.point.developer.bitdubai.version_1.structure.database;

import com.bitdubai.fermat_api.layer.all_definition.developer.DeveloperDatabase;
import com.bitdubai.fermat_api.layer.all_definition.developer.DeveloperDatabaseTable;
import com.bitdubai.fermat_api.layer.all_definition.developer.DeveloperDatabaseTableRecord;
import com.bitdubai.fermat_api.layer.all_definition.developer.DeveloperObjectFactory;
import com.bitdubai.fermat_api.layer.osa_android.database_system.Database;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseRecord;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseTable;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseTableRecord;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantLoadTableToMemoryException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by franklin on 14/10/15.
 */
public class DeveloperDatabaseFactory {
    String pluginId;
    List<String> walletsRedeemPoint;

    public DeveloperDatabaseFactory (String pluginId, List<String> walletsRedeemPoint){
        this.pluginId = pluginId;
        this.walletsRedeemPoint = walletsRedeemPoint;
    }

    public List<DeveloperDatabase> getDatabaseList(DeveloperObjectFactory developerObjectFactory) {
        /**
         * We have one database for each walletId, so we will return all their names.
         * Remember that a database name in this plug-in is the internal wallet id.
         */
        List<DeveloperDatabase> databases = new ArrayList<DeveloperDatabase>();
        for(String databaseName : this.walletsRedeemPoint)
            databases.add(developerObjectFactory.getNewDeveloperDatabase(databaseName, this.pluginId));
        return databases;
    }

    public static List<DeveloperDatabaseTable> getDatabaseTableList(DeveloperObjectFactory developerObjectFactory) {
        List<DeveloperDatabaseTable> tables = new ArrayList<DeveloperDatabaseTable>();


        /*
         * We only have one table in each database, let's complete it
         */
        List<String> assetWalletRedeemPointColumns = new ArrayList<>();
        assetWalletRedeemPointColumns.add(AssetWalletRedeemPointDatabaseConstant.ASSET_WALLET_REDEEM_POINT_TABLE_ID_COLUMN_NAME);
        assetWalletRedeemPointColumns.add(AssetWalletRedeemPointDatabaseConstant.ASSET_WALLET_REDEEM_POINT_ASSET_PUBLIC_KEY_COLUMN_NAME);
        assetWalletRedeemPointColumns.add(AssetWalletRedeemPointDatabaseConstant.ASSET_WALLET_REDEEM_POINT_ACTOR_FROM_COLUMN_NAME);
        assetWalletRedeemPointColumns.add(AssetWalletRedeemPointDatabaseConstant.ASSET_WALLET_REDEEM_POINT_ACTOR_FROM_TYPE_COLUMN_NAME);
        assetWalletRedeemPointColumns.add(AssetWalletRedeemPointDatabaseConstant.ASSET_WALLET_REDEEM_POINT_ACTOR_TO_COLUMN_NAME);
        assetWalletRedeemPointColumns.add(AssetWalletRedeemPointDatabaseConstant.ASSET_WALLET_REDEEM_POINT_ACTOR_TO_TYPE_COLUMN_NAME);
        assetWalletRedeemPointColumns.add(AssetWalletRedeemPointDatabaseConstant.ASSET_WALLET_REDEEM_POINT_ADDRESS_FROM_COLUMN_NAME);
        assetWalletRedeemPointColumns.add(AssetWalletRedeemPointDatabaseConstant.ASSET_WALLET_REDEEM_POINT_ADDRESS_TO_COLUMN_NAME);
        assetWalletRedeemPointColumns.add(AssetWalletRedeemPointDatabaseConstant.ASSET_WALLET_REDEEM_POINT_AMOUNT_COLUMN_NAME);
        assetWalletRedeemPointColumns.add(AssetWalletRedeemPointDatabaseConstant.ASSET_WALLET_REDEEM_POINT_BALANCE_TYPE_COLUMN_NAME);
        assetWalletRedeemPointColumns.add(AssetWalletRedeemPointDatabaseConstant.ASSET_WALLET_REDEEM_POINT_RUNNING_AVAILABLE_BALANCE_COLUMN_NAME);
        assetWalletRedeemPointColumns.add(AssetWalletRedeemPointDatabaseConstant.ASSET_WALLET_REDEEM_POINT_RUNNING_BOOK_BALANCE_COLUMN_NAME);
        assetWalletRedeemPointColumns.add(AssetWalletRedeemPointDatabaseConstant.ASSET_WALLET_REDEEM_POINT_TIME_STAMP_COLUMN_NAME);
        assetWalletRedeemPointColumns.add(AssetWalletRedeemPointDatabaseConstant.ASSET_WALLET_REDEEM_POINT_TRANSACTION_HASH_COLUMN_NAME);
        assetWalletRedeemPointColumns.add(AssetWalletRedeemPointDatabaseConstant.ASSET_WALLET_REDEEM_POINT_TYPE_COLUMN_NAME);
        assetWalletRedeemPointColumns.add(AssetWalletRedeemPointDatabaseConstant.ASSET_WALLET_REDEEM_POINT_MEMO_COLUMN_NAME);
        assetWalletRedeemPointColumns.add(AssetWalletRedeemPointDatabaseConstant.ASSET_WALLET_REDEEM_POINT_VERIFICATION_ID_COLUMN_NAME);

        /**
         * assetRedeemPointWalletColumns table
         */
        DeveloperDatabaseTable  cryptoTransactionsTable = developerObjectFactory.getNewDeveloperDatabaseTable(AssetWalletRedeemPointDatabaseConstant.ASSET_WALLET_REDEEM_POINT_TABLE_NAME, assetWalletRedeemPointColumns);
        tables.add(cryptoTransactionsTable);

        /**
         * Added new table AssetIssuerWalletTotalBalances
         */
        List<String> assetRedeemPointWalletTotalBalancesColumns = new ArrayList<>();
        assetRedeemPointWalletTotalBalancesColumns.add(AssetWalletRedeemPointDatabaseConstant.ASSET_WALLET_REDEEM_POINT_POINT_BALANCE_TABLE_ASSET_PUBLIC_KEY_COLUMN_NAME);
        assetRedeemPointWalletTotalBalancesColumns.add(AssetWalletRedeemPointDatabaseConstant.ASSET_WALLET_REDEEM_POINT_BALANCE_TABLE_NAME_COLUMN_NAME);
        assetRedeemPointWalletTotalBalancesColumns.add(AssetWalletRedeemPointDatabaseConstant.ASSET_WALLET_REDEEM_POINT_BALANCE_TABLE_DESCRIPTION_COLUMN_NAME);
        assetRedeemPointWalletTotalBalancesColumns.add(AssetWalletRedeemPointDatabaseConstant.ASSET_WALLET_REDEEM_POINT_BALANCE_TABLE_AVAILABLE_BALANCE_COLUMN_NAME);
        assetRedeemPointWalletTotalBalancesColumns.add(AssetWalletRedeemPointDatabaseConstant.ASSET_WALLET_REDEEM_POINT_BALANCE_TABLE_BOOK_BALANCE_COLUMN_NAME);
        assetRedeemPointWalletTotalBalancesColumns.add(AssetWalletRedeemPointDatabaseConstant.ASSET_WALLET_REDEEM_POINT_BALANCE_TABLE_QUANTITY_AVAILABLE_BALANCE_COLUMN_NAME);
        assetRedeemPointWalletTotalBalancesColumns.add(AssetWalletRedeemPointDatabaseConstant.ASSET_WALLET_REDEEM_POINT_BALANCE_TABLE_QUANTITY_BOOK_BALANCE_COLUMN_NAME);


        /**
         * AssetIssuerWalletTotalBalanceColumns table
         */
        DeveloperDatabaseTable  assetIssuerWalletWalletTotalBalances = developerObjectFactory.getNewDeveloperDatabaseTable(AssetWalletRedeemPointDatabaseConstant.ASSET_WALLET_REDEEM_POINT_BALANCE_TABLE_NAME, assetRedeemPointWalletTotalBalancesColumns);
        tables.add(assetIssuerWalletWalletTotalBalances);


        /**
         * REDEEM POINT STATISTICS TABLE.
         */
        List<String> statistics = new ArrayList<>();
        statistics.add(AssetWalletRedeemPointDatabaseConstant.ASSET_WALLET_REDEEM_POINT_POINT_STATISTIC_ID_COLUMN_NAME);
        statistics.add(AssetWalletRedeemPointDatabaseConstant.ASSET_WALLET_REDEEM_POINT_POINT_STATISTIC_ASSET_PUBLIC_KEY_COLUMN_NAME);
        statistics.add(AssetWalletRedeemPointDatabaseConstant.ASSET_WALLET_REDEEM_POINT_STATISTIC_USER_PUBLICKEY_COLUMN_NAME);
        statistics.add(AssetWalletRedeemPointDatabaseConstant.ASSET_WALLET_REDEEM_POINT_STATISTIC_REDEMPTION_TIMESTAMP_COLUMN_NAME);

        /**
         * AssetIssuerWalletTotalBalanceColumns table
         */
        DeveloperDatabaseTable statisticTable = developerObjectFactory.getNewDeveloperDatabaseTable(AssetWalletRedeemPointDatabaseConstant.ASSET_WALLET_REDEEM_POINT_STATISTIC_TABLE_NAME, statistics);
        tables.add(statisticTable);

        return tables;
    }

    public static List<DeveloperDatabaseTableRecord> getDatabaseTableContent(DeveloperObjectFactory developerObjectFactory,  Database database, DeveloperDatabaseTable developerDatabaseTable) {
        /**
         * Will get the records for the given table
         */
        List<DeveloperDatabaseTableRecord> returnedRecords = new ArrayList<DeveloperDatabaseTableRecord>();
        /**
         * I load the passed table name from the SQLite database.
         */
        DatabaseTable selectedTable = database.getTable(developerDatabaseTable.getName());
        try {
            selectedTable.loadToMemory();
            List<DatabaseTableRecord> records = selectedTable.getRecords();
            for (DatabaseTableRecord row: records){
                List<String> developerRow = new ArrayList<String>();
                /**
                 * for each row in the table list
                 */
                for (DatabaseRecord field : row.getValues()){
                    /**
                     * I get each row and save them into a List<String>
                     */
                    developerRow.add(field.getValue());
                }
                /**
                 * I create the Developer Database record
                 */
                returnedRecords.add(developerObjectFactory.getNewDeveloperDatabaseTableRecord(developerRow));
            }
            /**
             * return the list of DeveloperRecords for the passed table.
             */
        } catch (CantLoadTableToMemoryException cantLoadTableToMemory) {
            /**
             * if there was an error, I will returned an empty list.
             */
            database.closeDatabase();
            return returnedRecords;
        } catch (Exception e){
            database.closeDatabase();
            return returnedRecords;
        }
        database.closeDatabase();
        return returnedRecords;
    }
}
