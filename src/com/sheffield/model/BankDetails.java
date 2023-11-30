import java.sql.Date;
import java.util.List;
import java.util.Random;
import java.util.Base64;
import java.sql.ResultSet;
import java.sql.Connection;
import java.util.ArrayList;
import javax.crypto.Cipher;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.security.SecureRandom;
import javax.crypto.spec.SecretKeySpec;

/**
 * This class represents the bank details of a customer. It provides methods to
 * encrypt and decrypt sensitive information such as card name, card number,
 * security code, expiry date, and card holder name. It also generates a unique
 * bank detail ID for each instance of the class. The bank details are stored in
 * a MySQL database.
 * import javax.crypto.spec.SecretKeySpec
 */
public class BankDetails {

    private String cardName;
    private Date expiryDate;
    private String cardNumber;
    private String securityCode;
    private String cardHolderName;
    private static SecretKeySpec secretKey;
    private static final Random RAND = new Random();
    public static final int LAST_FOUR_DIGITS_LENGTH = 4;
    private static final List<Integer> generatedIds = new ArrayList<>();

    /**
     * Represents the bank details of a customer.
     * 
     * @param userId         The ID of the user.
     * @param cardName       The type of card.
     * @param cardHolderName The name of the card owner.
     * @param cardNumber     The card number.
     * @param expiryDate     The card's expiry date.
     * @param securityCode   The card's security code.
     */
    public BankDetails(int userId, String cardName, String cardHolderName, String cardNumber, Date expiryDate,
            String securityCode) {

        this.expiryDate = expiryDate;
        this.cardName = encrypt(cardName);
        this.cardNumber = encrypt(cardNumber);
        this.securityCode = encrypt(securityCode);
        this.cardHolderName = encrypt(cardHolderName);

        try {

            Connection conn = DriverManager.getConnection("jdbc:mysql://stusql.dcs.shef.ac.uk:3306/team020", "team020",
                    "asheet1Ie");

            // Check if email already exists in the database
            String checkQuery = "SELECT encryptedCardNumber FROM BankDetail WHERE encryptedCardNumber = ? AND encryptedSecurityCode = ?";
            PreparedStatement checkStatement = conn.prepareStatement(checkQuery);
            checkStatement.setString(1, this.cardNumber);
            checkStatement.setString(2, this.securityCode);
            ResultSet checkResult = checkStatement.executeQuery();

            if (checkResult.next()) {

                System.out.println("Error adding bank details: Card is already in use");
                checkResult.close();

            } else {

                PreparedStatement stmt = conn
                        .prepareStatement(
                                "INSERT INTO BankDetail (bankDetailID, userID, encryptedCardName, encryptedCardHolderName, encryptedCardNumber, encryptedExpiryDate, encryptedSecurityCode) VALUES (?, ?, ?, ?, ?, ?, ?)");
                stmt.setInt(1, generateBankDetailId());
                stmt.setInt(2, userId);
                stmt.setString(3, this.cardName);
                stmt.setString(4, this.cardHolderName);
                stmt.setString(5, this.cardNumber);
                stmt.setDate(6, this.expiryDate);
                stmt.setString(7, this.securityCode);
                stmt.executeUpdate();

                conn.close();
                stmt.close();
            }

        } catch (SQLException e) {

            e.printStackTrace();
        }
    }

    /**
     * Returns the decrypted card number.
     *
     * @return the decrypted card number
     */
    public String getCardNumber() {

        return decrypt(this.cardNumber);
    }

    /**
     * Returns the decrypted security code.
     *
     * @return the decrypted security code
     */
    public String getSecurityCode() {

        return decrypt(this.securityCode);
    }

    /**
     * Generates a new AES secret key for encryption and sets it as the current secret key.
     */
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

    
    /**
     * Encrypts the given string using AES encryption algorithm.
     *
     * @param strToEncrypt the string to be encrypted
     * @return the encrypted string
     */
    public static String encrypt( String strToEncrypt ) {

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

    /**
     * Decrypts a given string using the AES algorithm.
     * 
     * @param strToDecrypt the string to be decrypted
     * @return the decrypted string
     */
    public static String decrypt(String strToDecrypt) {

        try {

            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
            
        } catch ( Exception e ) {
            
            System.out.println("Error while decrypting: " + e.toString());
        }

        return null;
    }

    /**
     * Hides the card details by replacing the card number with asterisks and
     * keeping the last four digits visible.
     * 
     * @param cardNumber   The card number to be hidden.
     * @param securityCode The security code of the card.
     * @return The hidden card number with the last four digits visible.
     */
    public static String hideCardDetails(String cardNumber, String securityCode) {

        String hiddenCardNumber = "";
        String hiddenSecurityCode = "***";
        int length = cardNumber.length();

        if (length >= LAST_FOUR_DIGITS_LENGTH) {

            String lastFourDigits = cardNumber.substring(length - 4);
            hiddenCardNumber = "**** " + lastFourDigits;
        }

        return hiddenCardNumber;
    }

    /**
     * Generates a unique bank detail ID.
     * 
     * @return The generated bank detail ID.
     */
    public int generateBankDetailId() {

        int newId = RAND.nextInt(900000) + 100000;

        if (generatedIds.contains(newId)) {

            newId = RAND.nextInt(900000) + 100000;
        }

        generatedIds.add(newId);
        return newId;
    }
}