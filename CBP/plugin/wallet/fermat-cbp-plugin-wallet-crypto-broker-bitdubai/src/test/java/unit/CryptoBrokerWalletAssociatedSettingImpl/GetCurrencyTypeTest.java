package unit.CryptoBrokerWalletAssociatedSettingImpl;

import com.bitdubai.fermat_cbp_api.all_definition.enums.CurrencyType;
import com.bitdubai.fermat_cbp_plugin.layer.wallet.crypto_broker.developer.bitdubai.version_1.structure.util.CryptoBrokerWalletAssociatedSettingImpl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by José Vilchez on 21/01/16.
 */
@RunWith(MockitoJUnitRunner.class)
public class GetCurrencyTypeTest {

    @Test
    public void getCurrencyType(){
        CryptoBrokerWalletAssociatedSettingImpl cryptoBrokerWalletAssociatedSetting = mock(CryptoBrokerWalletAssociatedSettingImpl.class);
        when(cryptoBrokerWalletAssociatedSetting.getCurrencyType()).thenReturn(CurrencyType.CRYPTO_MONEY);
        assertThat(cryptoBrokerWalletAssociatedSetting.getCurrencyType()).isNotNull();
    }

}
