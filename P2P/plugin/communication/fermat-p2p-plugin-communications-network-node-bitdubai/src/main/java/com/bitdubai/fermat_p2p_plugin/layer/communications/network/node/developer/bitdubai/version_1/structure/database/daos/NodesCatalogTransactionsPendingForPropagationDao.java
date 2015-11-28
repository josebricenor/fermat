/*
 * @#NodesCatalogTransactionsPendingForPropagationDao.java - 2015
 * Copyright bitDubai.com., All rights reserved.
 * You may not modify, use, reproduce or distribute this software.
 * BITDUBAI/CONFIDENTIAL
 */
package com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.daos;

import com.bitdubai.fermat_api.layer.all_definition.exceptions.InvalidParameterException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.Database;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseTableRecord;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.CommunicationsNetworkNodeP2PDatabaseConstants;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.entities.NodesCatalogTransactionsPendingForPropagation;

import java.sql.Timestamp;

/**
 * The Class <code>com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.daos.NodesCatalogTransactionsPendingForPropagationDao</code>
 * <p/>
 * Created by Roberto Requena - (rart3001@gmail.com) on 23/11/15.
 *
 * @version 1.0
 * @since Java JDK 1.7
 */
public class NodesCatalogTransactionsPendingForPropagationDao  extends AbstractBaseDao<NodesCatalogTransactionsPendingForPropagation> {

    /**
     * Constructor with parameter
     *
     * @param dataBase
     */
    public NodesCatalogTransactionsPendingForPropagationDao(Database dataBase) {
        super(dataBase, CommunicationsNetworkNodeP2PDatabaseConstants.NODES_CATALOG_TRANSACTIONS_PENDING_FOR_PROPAGATION_TABLE_NAME, CommunicationsNetworkNodeP2PDatabaseConstants.NODES_CATALOG_TRANSACTIONS_PENDING_FOR_PROPAGATION_FIRST_KEY_COLUMN);
    }

    /**
     * (non-javadoc)
     * @see AbstractBaseDao#getEntityFromDatabaseTableRecord(DatabaseTableRecord)
     */
    @Override
    protected NodesCatalogTransactionsPendingForPropagation getEntityFromDatabaseTableRecord(DatabaseTableRecord record) throws InvalidParameterException {

        NodesCatalogTransactionsPendingForPropagation entity = new NodesCatalogTransactionsPendingForPropagation();

        entity.setHashId(record.getStringValue(CommunicationsNetworkNodeP2PDatabaseConstants.NODES_CATALOG_TRANSACTIONS_PENDING_FOR_PROPAGATION_HASH_ID_COLUMN_NAME));
        entity.setLastConnectionTimestamp(new Timestamp(record.getLongValue(CommunicationsNetworkNodeP2PDatabaseConstants.NODES_CATALOG_TRANSACTIONS_PENDING_FOR_PROPAGATION_LAST_CONNECTION_TIMESTAMP_COLUMN_NAME)));
        entity.setDefaultPort(record.getIntegerValue(CommunicationsNetworkNodeP2PDatabaseConstants.NODES_CATALOG_TRANSACTIONS_PENDING_FOR_PROPAGATION_DEFAULT_PORT_COLUMN_NAME));
        entity.setIdentityPublicKey(record.getStringValue(CommunicationsNetworkNodeP2PDatabaseConstants.NODES_CATALOG_TRANSACTIONS_PENDING_FOR_PROPAGATION_IDENTITY_PUBLIC_KEY_COLUMN_NAME));
        entity.setIp(record.getStringValue(CommunicationsNetworkNodeP2PDatabaseConstants.NODES_CATALOG_TRANSACTIONS_PENDING_FOR_PROPAGATION_IP_COLUMN_NAME));
        entity.setName(record.getStringValue(CommunicationsNetworkNodeP2PDatabaseConstants.NODES_CATALOG_TRANSACTIONS_PENDING_FOR_PROPAGATION_NAME_COLUMN_NAME));
        entity.setLastLatitude(record.getDoubleValue(CommunicationsNetworkNodeP2PDatabaseConstants.NODES_CATALOG_TRANSACTIONS_PENDING_FOR_PROPAGATION_LAST_LATITUDE_COLUMN_NAME));
        entity.setLastLongitude(record.getDoubleValue(CommunicationsNetworkNodeP2PDatabaseConstants.NODES_CATALOG_TRANSACTIONS_PENDING_FOR_PROPAGATION_LAST_LONGITUDE_COLUMN_NAME));
        entity.setRegisteredTimestamp(new Timestamp(record.getLongValue(CommunicationsNetworkNodeP2PDatabaseConstants.NODES_CATALOG_TRANSACTIONS_PENDING_FOR_PROPAGATION_REGISTERED_TIMESTAMP_COLUMN_NAME)));
        entity.setTransactionType(record.getStringValue(CommunicationsNetworkNodeP2PDatabaseConstants.NODES_CATALOG_TRANSACTIONS_PENDING_FOR_PROPAGATION_TRANSACTION_TYPE_COLUMN_NAME));

        return entity;
    }

    /**
     * (non-javadoc)
     * @see AbstractBaseDao#getDatabaseTableRecordFromEntity
     */
    @Override
    protected DatabaseTableRecord getDatabaseTableRecordFromEntity(NodesCatalogTransactionsPendingForPropagation entity) {

        DatabaseTableRecord databaseTableRecord = getDatabaseTable().getEmptyRecord();

        databaseTableRecord.setStringValue(CommunicationsNetworkNodeP2PDatabaseConstants.NODES_CATALOG_TRANSACTIONS_PENDING_FOR_PROPAGATION_HASH_ID_COLUMN_NAME, entity.getHashId());
        databaseTableRecord.setLongValue(CommunicationsNetworkNodeP2PDatabaseConstants.NODES_CATALOG_TRANSACTIONS_PENDING_FOR_PROPAGATION_LAST_CONNECTION_TIMESTAMP_COLUMN_NAME, entity.getLastConnectionTimestamp().getTime());
        databaseTableRecord.setIntegerValue(CommunicationsNetworkNodeP2PDatabaseConstants.NODES_CATALOG_TRANSACTIONS_PENDING_FOR_PROPAGATION_DEFAULT_PORT_COLUMN_NAME, entity.getDefaultPort());
        databaseTableRecord.setStringValue(CommunicationsNetworkNodeP2PDatabaseConstants.NODES_CATALOG_TRANSACTIONS_PENDING_FOR_PROPAGATION_IDENTITY_PUBLIC_KEY_COLUMN_NAME, entity.getIdentityPublicKey());
        databaseTableRecord.setStringValue(CommunicationsNetworkNodeP2PDatabaseConstants.NODES_CATALOG_TRANSACTIONS_PENDING_FOR_PROPAGATION_IP_COLUMN_NAME, entity.getIp());
        databaseTableRecord.setStringValue(CommunicationsNetworkNodeP2PDatabaseConstants.NODES_CATALOG_TRANSACTIONS_PENDING_FOR_PROPAGATION_NAME_COLUMN_NAME, entity.getName());
        databaseTableRecord.setDoubleValue(CommunicationsNetworkNodeP2PDatabaseConstants.NODES_CATALOG_TRANSACTIONS_PENDING_FOR_PROPAGATION_LAST_LATITUDE_COLUMN_NAME, entity.getLastLatitude());
        databaseTableRecord.setDoubleValue(CommunicationsNetworkNodeP2PDatabaseConstants.NODES_CATALOG_TRANSACTIONS_PENDING_FOR_PROPAGATION_LAST_LONGITUDE_COLUMN_NAME, entity.getLastLongitude());
        databaseTableRecord.setLongValue(CommunicationsNetworkNodeP2PDatabaseConstants.NODES_CATALOG_TRANSACTIONS_PENDING_FOR_PROPAGATION_REGISTERED_TIMESTAMP_COLUMN_NAME, entity.getRegisteredTimestamp().getTime());
        databaseTableRecord.setStringValue(CommunicationsNetworkNodeP2PDatabaseConstants.NODES_CATALOG_TRANSACTIONS_PENDING_FOR_PROPAGATION_TRANSACTION_TYPE_COLUMN_NAME, entity.getTransactionType());

        return databaseTableRecord;
    }
}
