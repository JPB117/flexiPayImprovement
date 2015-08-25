package com.workpoint.icpak.server.integration.lms;

import javax.ws.rs.ext.ContextResolver;

import com.sun.jersey.api.json.JSONJAXBContext;
import com.workpoint.icpak.shared.model.MemberDto;
import com.workpoint.icpak.shared.model.UserDto;

public class JAXBProviderImpl implements ContextResolver<JSONJAXBContext> {

	private JSONJAXBContext jsonJAXBContext;

	@Override
	public JSONJAXBContext getContext(Class<?> arg0) {


		if (!(arg0.equals(MemberDto.class)
				|| arg0.equals(UserDto.class))) {
			return null;
		}

		if (jsonJAXBContext != null) {
			return jsonJAXBContext;
		}

		try {
			jsonJAXBContext = new JSONJAXBContext(MemberDto.class,
					UserDto.class);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}

		return jsonJAXBContext;
	}
}
