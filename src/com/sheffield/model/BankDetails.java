import java.sql.*;
import java.sql.Date;
import javax.crypto.*;
import java.util.*;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.Cipher;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.KeySpec;


/**
 * This class represents the bank details of a customer. It provides methods to
 * encrypt and decrypt sensitive information such as card name, card number,
 * security code, expiry date, and card holder name. It also generates a unique
 * bank detail ID for each instance of the class. The bank details are stored in
 * a MySQL database.
import javax.crypto.spec.SecretKeySpec
 */
public class BankDetails {

    private String cardName;
    private static byte[] key;
    private Date expiryDate;
    private String cardNumber;
    private String securityCode;
    private String cardHolderName; 
    private static SecretKeySpec secretKey;
    private static final Random RAND = new Random();

    public BankDetails(int customerId, String cardName, String cardHolderName, String cardNumber, Date expiryDate,
            String securityCode) {

        this.cardName = encrypt(cardName);
        this.cardNumber = encrypt(cardNumber);
        this.securityCode = encrypt(securityCode);
        this.expiryDate = expiryDate;
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
            stmt.setDate(6, this.expiryDate);
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

    
    public static void setKey() {

        try {

            SecureRandom secureRandom = new SecureRandom();
            byte[] key = new byte[16];
            secureRandom.nextBytes(key);
            secretKey = new SecretKeySpec(key, "AES");

        } catch (Exception e) {

            System.out.println("Error while generating key: " + e.toString());
        }
    }

    public static String encrypt(String strToEncrypt) {

        try {

            setKey();
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));

        } catch (Exception e) {

            System.out.println("Error while encrypting: " + e.toString());
        }

        return null;
    }

    public static String decrypt(String strToDecrypt) {

        try {

            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
            
        } catch (Exception e) {
            
            System.out.println("Error while decrypting: " + e.toString());
        }

        return null;
    }

public int generateBankDetailId() {

        return RAND.nextInt(900000) + 100000;
    }
}