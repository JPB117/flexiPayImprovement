package com.workpoint.icpak.tests.base;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.net.URI;
import java.net.URISyntaxException;

import javax.persistence.EntityManager;
import javax.ws.rs.core.UriInfo;

import org.apache.shiro.subject.Subject;
import org.jukito.JukitoRunner;
import org.jukito.UseModules;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import com.google.inject.Inject;
import com.icpak.rest.dao.UsersDao;
import com.icpak.rest.dao.helper.UsersDaoHelper;

@RunWith(JukitoRunner.class)
@UseModules({BaseModule.class})
public abstract class AbstractDaoTest extends AbstractShiroTest{

	@Inject UsersDaoHelper helper;
	@Inject UsersDao dao;
	
	@Inject EntityManager em;
	
	@Mock
	protected UriInfo uriInfo;
	
	@Before
	public void init() throws URISyntaxException{
		Subject subject = mock(Subject.class);
		when(subject.getPrincipal()).thenReturn("Administrator");
		
		uriInfo = mock(UriInfo.class);
	    when(uriInfo.getAbsolutePath()).thenReturn(new URI("www.wira.io" + "/icpak/api/"));

	}
	
	protected void commit(){
		em.getTransaction().commit();
	}
	
	protected void rollback(){
		em.getTransaction().rollback();
	}
	
}
