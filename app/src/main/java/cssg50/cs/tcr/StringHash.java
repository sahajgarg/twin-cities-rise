package cssg50.cs.tcr;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by ruiaguiar1 on 6/2/17.
 * Class for hashing passwords so they are not stored directly
 */

//Class to hash strings in a cryptographically secure manner. Used for password security.
 class StringHash {

      StringHash(){};

     String hashString(String str){
        MessageDigest m;
        //Uses secure MD5 hash to store passwords
        try {
            m = MessageDigest.getInstance("MD5");
        }
        catch (NoSuchAlgorithmException ex){
            return "failed";
        }
        m.reset();
        m.update(str.getBytes());
        byte[] digest = m.digest();
        BigInteger bigInt = new BigInteger(1,digest);
        String hashtext = bigInt.toString(16);
        // Now we need to zero pad it if you actually want the full 32 chars.
        while(hashtext.length() < 32 ){
            hashtext = "0"+hashtext;
        }
        return hashtext;
    }
}
