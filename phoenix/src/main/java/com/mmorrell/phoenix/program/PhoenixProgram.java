package com.mmorrell.phoenix.program;

import com.mmorrell.phoenix.model.LimitOrderPacketRecord;
import org.p2p.solanaj.core.AccountMeta;
import org.p2p.solanaj.core.PublicKey;
import org.p2p.solanaj.core.TransactionInstruction;
import org.p2p.solanaj.programs.Program;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;


public class PhoenixProgram extends Program {

    public static final PublicKey PHOENIX_PROGRAM_ID =
            PublicKey.valueOf("PhoeNiXZ8ByJGLkxNfZRnkUfjvmuYqLR89jjFHGqdXY");
    private static final PublicKey TOKEN_PROGRAM_ID =
            PublicKey.valueOf("TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA");
    private static final PublicKey SYSVAR_RENT_PUBKEY =
            PublicKey.valueOf("SysvarRent111111111111111111111111111111111");


    public static TransactionInstruction placeLimitOrder(PublicKey market, PublicKey trader,
                                                         PublicKey seat, PublicKey baseAccount,
                                                         PublicKey quoteAccount, PublicKey baseVault,
                                                         PublicKey quoteVault, LimitOrderPacketRecord limitOrderPacketRecord) {
        List<AccountMeta> accountMetas = new ArrayList<>();

        accountMetas.add(new AccountMeta(PHOENIX_PROGRAM_ID, false, false));
        accountMetas.add(new AccountMeta(PhoenixSeatManagerProgram.PHOENIX_LOG_AUTHORITY_ID, false, false));
        accountMetas.add(new AccountMeta(market, false, true));
        accountMetas.add(new AccountMeta(trader, true, false));
        accountMetas.add(new AccountMeta(seat, false, false));
        accountMetas.add(new AccountMeta(baseAccount, false, true));
        accountMetas.add(new AccountMeta(quoteAccount, false, true));
        accountMetas.add(new AccountMeta(baseVault, false, true));
        accountMetas.add(new AccountMeta(quoteVault, false, true));
        accountMetas.add(new AccountMeta(TOKEN_PROGRAM_ID, false, false));

        byte[] transactionData = encodePlaceLimitOrderInstruction(limitOrderPacketRecord);

        return createTransactionInstruction(
                PHOENIX_PROGRAM_ID,
                accountMetas,
                transactionData
        );
    }

    private static byte[] encodePlaceLimitOrderInstruction(LimitOrderPacketRecord limitOrderPacketRecord) {
        ByteBuffer result = ByteBuffer.allocate(40);
        result.order(ByteOrder.LITTLE_ENDIAN);

        result.put(0, (byte) 2);
        result.put(1, (byte) 1);

        // index 2 = 0 for bid, 1 for ask
        result.put(2, limitOrderPacketRecord.toBytes());

        return result.array();
    }
}