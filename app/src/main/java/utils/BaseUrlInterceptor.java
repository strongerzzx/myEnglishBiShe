package utils;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class BaseUrlInterceptor implements Interceptor {
    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {
        Request request=chain.request();
        HttpUrl oldUrl = request.url();
        Request.Builder builder = request.newBuilder();
        List<String> headers = request.headers("bqs_auth");
        if (headers != null && headers.size()>0) {
            builder.removeHeader("bqs_auth");
            String headerValue = headers.get(0);
            HttpUrl newBaseUrl=null;

        }

        return null;
    }
}
