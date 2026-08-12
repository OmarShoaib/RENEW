package edu.aku.omarshoaib.renew.webcall.web_client;

import android.app.Activity;

import edu.aku.omarshoaib.renew.global.AppConstants;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class WebClient {

    private static WebClient instance;
    private final WebAPI webAPI;
    private static String BASE_URL;
    // Dictionary Portal Url
    public static String DICTIONARY_PORTAL_URL;

    private WebClient(Activity activity) {
        // This logic of initializing BASE_URL is to generalize the code and control the
        // toggling from AppConstants. Also, it will initialized only once because we
        // use Singleton pattern.
        String HOST_NAME;
//        if (AppConstants.IS_PRODUCTION_SERVER) {
//            // Production Base Url
//        } else {
//            // Testing Base Url
//            HOST_NAME = "cls-pae-fp79887";
//        }
        HOST_NAME = AppConstants.IS_PRODUCTION_SERVER ? "vcoe1.aku.edu" : "cls-pae-fp79887";
        BASE_URL = "https://" + HOST_NAME + "/";
        // Dictionary portal url for downloading strings and ranges
        DICTIONARY_PORTAL_URL = BASE_URL + "dictionary_portal/api/";

//        // For logging
//        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
//        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
//
//        OkHttpClient.Builder httpBuilder = new OkHttpClient.Builder();
//        /*// For SSL
//        httpBuilder.sslSocketFactory(Objects.requireNonNull(CryptoUtil.getSSLContext(activity)).getSocketFactory(),
//                CryptoUtil.getX509TrustManager());*/
//
//        // For certificate pinning
//        CertificatePinner certificatePinner = new CertificatePinner.Builder()
//                .add(HOST_NAME, BuildConfig.CERT_KEY)
//                .add(HOST_NAME, BuildConfig.CERT_KEY_BACKUP)
//                .build();
//        httpBuilder.certificatePinner(certificatePinner);
//        // For certificate validation
//        httpBuilder.hostnameVerifier((hostname, session) -> CryptoUtil.checkCertValidity(activity, session));
//        httpBuilder.addInterceptor(loggingInterceptor);
//        // For adding user agent
////        httpBuilder.addInterceptor(new UserAgentInterceptor(USER_AGENT));
//        // For connection timeout
//        httpBuilder.connectTimeout(AppConstants.CONNECTION_TIMEOUT, TimeUnit.SECONDS);
//        // For read timeout
//        httpBuilder.readTimeout(AppConstants.CONNECTION_TIMEOUT, TimeUnit.SECONDS);
//
//        OkHttpClient okHttpClient = httpBuilder.build();

        OkHttpClient okHttpClient = CryptoUtil.generateSecureOkHttpClient(activity, HOST_NAME);

        Retrofit retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
//                .addConverterFactory(GsonConverterFactory.create())
                .build();
        webAPI = retrofit.create(WebAPI.class);
    }

    public static synchronized WebClient getInstance(Activity activity) {
        if(instance == null) {
            instance = new WebClient(activity);
        }
        return instance;
    }

    public static synchronized void resetInstance() {
        instance = null;
    }

    public WebAPI getWebAPI() {
        return webAPI;
    }

    /*This interceptor adds a custom User-Agent*/
    /*public static class UserAgentInterceptor implements Interceptor {

        private final String userAgent;

        public UserAgentInterceptor(String userAgent) {
            this.userAgent = userAgent;
        }

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request originalRequest = chain.request();
            Request requestWithUserAgent = originalRequest.newBuilder()
                    .header("User-Agent", userAgent)
                    .build();
            return chain.proceed(requestWithUserAgent);
        }
    }*/

    public static String getBaseUrl() {
        return BASE_URL;
    }

}
