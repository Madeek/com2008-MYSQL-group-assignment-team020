import java.sql.*;
import javax.crypto.*;
import java.util.*;
import javax.crypto.spec.SecretKeySpec;

/**
 * This class represents the bank details of a customer. It provides methods to
 * encrypt and decrypt sensitive information such as card name, card number,
 * security code, expiry date, and card holder name. It also generates a unique
 * bank detail ID for each instance of the class. The bank details are stored in
 * a MySQL database.
 */
public class BankDetails {

    private String cardName;
    private String cardNumber;
    private String securityCode;
    private String expiryDate;
    private String cardHolderName;
    private static final String ALGORITHM = "AES/CBC/PKCS5Padding";
    private static final Random RAND = new Random();
    private static final byte[] KEY = "mySecretKey12345".getBytes();

    public BankDetails(int customerId, String cardName, String cardHolderName, String cardNumber, String expiryDate,
            String securityCode) {

        this.cardName = encrypt(cardName);
        this.cardNumber = encrypt(cardNumber);
        this.securityCode = encrypt(securityCode);
        this.expiryDate = encrypt(expiryDate);
        this.cardHolderName = encrypt(cardHolderName);

        try {

            Connection conn = DriverManager.getConnection("jdbc:mysql://stusql.dcs.shef.ac.uk:3306/team020", "team020",
                    "asheet1Ie");
            PreparedStatement stmt = conn
                    .prepareStatement(
                            "INSERT INTO BankDetail (bankDetailID, userID, encryptedCardName, encryptedCardHolderName, encryptedCardNumber, encryptedExpiryDate, encryptedSecurityCode) VALUES (?, ?, ?, ?, ?, ?, ?)");
            stmt.setInt(1, generateBankDetailId());
            stmt.setInt(2, customerId);
            stmt.setString(3, this.cardName);
            stmt.setString(4, this.cardHolderName);
            stmt.setString(5, this.cardNumber);
            stmt.setString(6, this.expiryDate);
            stmt.setString(7, this.securityCode);
            stmt.executeUpdate();

        } catch (SQLException e) {

            e.printStackTrace();
        }
    }

    public String getCardNumber() {

        return decrypt(this.cardNumber);
    }

    public String expiryDate() {

        return decrypt(this.expiryDate);
    }

    public String getSecurityCode() {

        return decrypt(this.securityCode);
    }

    private static String encrypt(String value) {

        try {

            SecretKeySpec key = new SecretKeySpec(KEY, ALGORITHM);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] encryptedValue = cipher.doFinal(value.getBytes());
            return Base64.getEncoder().encodeToString(encryptedValue);

        } catch (Exception e) {

            e.printStackTrace();
            return null;
        }
    }

    private static String decrypt(String encryptedValue) {

        try {

            SecretKeySpec key = new SecretKeySpec(KEY, ALGORITHM);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] decryptedValue = cipher.doFinal(Base64.getDecoder().decode(encryptedValue));
            return new String(decryptedValue);

        } catch (Exception e) {

            e.printStackTrace();
            return null;
        }
    }

    private int generateBankDetailId() {

        return RAND.nextInt(900000) + 100000;
    }
}