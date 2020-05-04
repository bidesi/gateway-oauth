package in.sabnar.gateway.filter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

public class AuthenticatedFilter extends ZuulFilter{
	@Override
	public String filterType() {
		return "pre";
	}

	@Override
	public int filterOrder() {
		return 10;
	}

	@Override
	public boolean shouldFilter() {
		return true;
	}

	@Override
	public Object run() {
		RequestContext context = RequestContext.getCurrentContext();
		Map<String, List<String>> newParameterMap = new HashMap<>();
		Map<String, String[]> parameterMap = context.getRequest().getParameterMap();
		// getting the current parameter
		for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
			String key = entry.getKey();
			String[] values = entry.getValue();
			newParameterMap.put(key, Arrays.asList(values));
		}
		// add a new parameter
		String authenticatedKey = "authenticated";
		String authenticatedValue = "true";
		newParameterMap.put(authenticatedKey, Arrays.asList(authenticatedValue));
		context.setRequestQueryParams(newParameterMap);
		return null;
	}
}
