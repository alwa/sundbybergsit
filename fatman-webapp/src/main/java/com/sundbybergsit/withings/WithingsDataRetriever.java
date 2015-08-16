package com.sundbybergsit.withings;

public class WithingsDataRetriever {

    public void login(String ga) {
        String url = "https://oauth.withings.com/account/authorize?oauth_consumer_key=0e0e7e6911defb09f82084e5bb56ff95268d5e4728777c743c1cfcdf7cbc19&oauth_nonce=a7407b8e347e98fdeddeab2c136193eb&oauth_signature=j9jX%2F6yglkGcdHQPfCre9wO4%2BYY%3D&oauth_signature_method=HMAC-SHA1&oauth_timestamp=1438620590&oauth_token=e7f77cc28e6fbc7937755585ccb29c6613334b086488171e3bee6b88c8fb6&oauth_version=1.0";

        // Step 2
        //userid=2197524&oauth_token=e7f77cc28e6fbc7937755585ccb29c6613334b086488171e3bee6b88c8fb6&oauth_verifier=bTSYm99kBDTfpQ8cs13

        // Step 3
        //
        //https://oauth.withings.com/account/access_token?oauth_consumer_key=0e0e7e6911defb09f82084e5bb56ff95268d5e4728777c743c1cfcdf7cbc19&oauth_nonce=cbb3575e9b8cf6a556dca7d9f057d6ef&oauth_signature=tEzVl4bWyOPbLO0kkJssqV2rs48%3D&oauth_signature_method=HMAC-SHA1&oauth_timestamp=1438620942&oauth_token=e7f77cc28e6fbc7937755585ccb29c6613334b086488171e3bee6b88c8fb6
    }


}
