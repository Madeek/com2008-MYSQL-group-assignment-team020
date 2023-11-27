import java.sql.*;
import java.sql.Date;
import java.util.*;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.Cipher;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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
    private Date expiryDate;
    private String cardNumber;
    private String securityCode;
    private String cardHolderName; 
    private static SecretKeySpec secretKey;
    private static final Random RAND = new Random();
    private static final String SALT = "EncryptedString";
    private static final List<Integer> generatedIds = new ArrayList<>();


    public BankDetails(int customerId, String cardName, String cardHolderName, String cardNumber, Date expiryDate,
            String securityCode) {

        this.cardName = encrypt( cardName.toCharArray() );
        this.cardNumber = encrypt( cardNumber.toCharArray() );
        this.securityCode = encrypt( securityCode.toCharArray() );
        this.expiryDate = expiryDate;
        this.cardHolderName = encrypt( cardHolderName.toCharArray() );

        try {

            Connection conn = DriverManager.getConnection("jdbc:mysql://stusql.dcs.shef.ac.uk:3306/team020", "team020", "asheet1Ie");
            PreparedStatement stmt = conn
                    .prepareStatement(
                            "INSERT INTO BankDetail (bankDetailID, userID, encryptedCardName, encryptedCardHolderName, encryptedCardNumber, encryptedExpiryDate, encryptedSecurityCode) VALUES (?, ?, ?, ?, ?, ?, ?)");
            stmt.setInt( 1, generateBankDetailId() );
            stmt.setInt( 2, customerId );
            stmt.setString( 3, this.cardName );
            stmt.setString( 4, this.cardHolderName );
            stmt.setString( 5, this.cardNumber );
            stmt.setDate( 6, this.expiryDate );
            stmt.setString( 7, this.securityCode );
            stmt.executeUpdate();

        } catch ( SQLException e ) {

            e.printStackTrace();
        }
    }

    public String getCardNumber() {

        return decrypt( this.cardNumber );
    }

    public String getSecurityCode() {

        return decrypt( this.securityCode );
    }

    public static String encrypt( char[] strToEncrypt ) {

        try {

            // Create a MessageDigest instance for SHA-256
            MessageDigest md = MessageDigest.getInstance( "SHA-256" );

            // Concatenate the salt and password bytes
            byte[] saltedPasswordBytes = concatenateBytes( SALT.getBytes(), new String( strToEncrypt ).getBytes() );

            // Update the digest with the salted password bytes
            md.update( saltedPasswordBytes );

            // Get the hashed password bytes
            byte[] hashedPasswordBytes = md.digest();

            // Convert the hashed password bytes to a hexadecimal string
            StringBuilder hexStringBuilder = new StringBuilder();

            for ( byte b : hashedPasswordBytes ) {

                hexStringBuilder.append( String.format( "%02x", b ) );
            }

            return hexStringBuilder.toString();

        } catch ( NoSuchAlgorithmException e ) {

            // Handle the exception, e.g., log it or throw a custom exception
            e.printStackTrace();
            return null;
        }
    }

    public static byte[] concatenateBytes( byte[] arr1, byte[] arr2 ) {
        
        byte[] combined = new byte[ arr1.length + arr2.length ];
        System.arraycopy( arr1, 0, combined, 0, arr1.length );
        System.arraycopy( arr2, 0, combined, arr1.length, arr2.length );
        return combined;
    }


    public static String decrypt( String strToDecrypt ) {

        try {

            Cipher cipher = Cipher.getInstance( "SHA-256" );
            cipher.init( Cipher.DECRYPT_MODE, secretKey );
            return new String( cipher.doFinal(Base64.getDecoder().decode( strToDecrypt ) ) );
            
        } catch ( Exception e ) {
            
            System.out.println( "Error while decrypting: " + e.toString() );
        }

        return null;
    }

    public int generateBankDetailId() {

        int newId = RAND.nextInt( 900000 ) + 100000;

        if ( generatedIds.contains( newId ) ) {

            newId = RAND.nextInt( 900000 ) + 100000;
        }
        
        generatedIds.add( newId );
        return newId;
    }
}