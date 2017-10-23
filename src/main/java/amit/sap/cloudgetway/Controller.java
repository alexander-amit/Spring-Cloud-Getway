package amit.sap.cloudgetway;

import java.net.URI;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.cloud.gateway.mvc.ProxyExchange;

@RestController
public class Controller {
	@Value("${remote.home}")
	private URI home;

	@GetMapping(path = "/test", headers = "x-host=png.abc.org")
	public ResponseEntity<Object> proxy(ProxyExchange<Object> proxy) throws Exception {
		System.out.println("Step 1");
		return proxy.uri(home.toString() + "/image/png").get(header("X-TestHeader", "foobar"));
	}

	@GetMapping("/test2")
	public ResponseEntity<Object> proxyFoos(ProxyExchange<Object> proxy) throws Exception {
		System.out.println("Step 2");
		return proxy.uri(home.toString() + "/image/webp").get(header("X-AnotherHeader", "baz"));
	}

	private Function<ResponseEntity<Object>, ResponseEntity<Object>> header(String key, String value) {
		System.out.println("Step 3");
		return response -> ResponseEntity.status(response.getStatusCode()).headers(response.getHeaders())
				.header(key, value).body(response.getBody());
	}

}
