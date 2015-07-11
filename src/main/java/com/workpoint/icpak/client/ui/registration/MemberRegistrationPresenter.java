package com.workpoint.icpak.client.ui.registration;

import java.util.List;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.gwt.user.client.ui.Anchor;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.common.client.IndirectProvider;
import com.gwtplatform.common.client.StandardProvider;
import com.gwtplatform.dispatch.rest.delegates.client.ResourceDelegate;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ContentSlot;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.NoGatekeeper;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.gwtplatform.mvp.client.proxy.RevealContentHandler;
import com.gwtplatform.mvp.client.proxy.RevealRootContentEvent;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;
import com.workpoint.icpak.client.place.NameTokens;
import com.workpoint.icpak.client.service.AbstractAsyncCallback;
import com.workpoint.icpak.client.service.ServiceCallback;
import com.workpoint.icpak.client.ui.component.ActionLink;
import com.workpoint.icpak.client.ui.component.TextField;
import com.workpoint.icpak.client.ui.error.ErrorPresenter;
import com.workpoint.icpak.client.ui.events.ErrorEvent;
import com.workpoint.icpak.client.ui.events.ErrorEvent.ErrorHandler;
import com.workpoint.icpak.shared.api.ApplicationFormResource;
import com.workpoint.icpak.shared.api.CategoriesResource;
import com.workpoint.icpak.shared.api.InvoiceResource;
import com.workpoint.icpak.shared.api.UsersResource;
import com.workpoint.icpak.shared.model.ApplicationCategoryDto;
import com.workpoint.icpak.shared.model.ApplicationFormHeaderDto;
import com.workpoint.icpak.shared.model.InvoiceDto;
import com.workpoint.icpak.shared.model.UserDto;

public class MemberRegistrationPresenter
		extends
		Presenter<MemberRegistrationPresenter.MyView, MemberRegistrationPresenter.MyProxy> 
implements ErrorHandler{

	public interface MyView extends View {
		ApplicationFormHeaderDto getApplicationForm();

		HasClickHandlers getANext();

		HasClickHandlers getABack();

		Anchor getActivateAccLink();

		TextField getEmail();

		void setEmailValid(boolean isValid);

		void setCategories(List<ApplicationCategoryDto> dtos);

		boolean isValid();

		void bindForm(ApplicationFormHeaderDto result);

		void next();

		int getCounter();

		void setLoadingState(ActionLink anchor, boolean isLoading);

		void showError(String string);

		void setMiddleHeight();

		void bindInvoice(InvoiceDto invoice);
	}

	@ProxyCodeSplit
	@NameToken(NameTokens.signup)
	@NoGatekeeper
	public interface MyProxy extends ProxyPlace<MemberRegistrationPresenter> {
	}

	@ContentSlot
	public static final Type<RevealContentHandler<?>> DOCPOPUP_SLOT = new Type<RevealContentHandler<?>>();

	@Inject
	PlaceManager placeManager;
	
	IndirectProvider<ErrorPresenter> errorFactory;

	private String refid;

	private ResourceDelegate<UsersResource> usersDelegate;

	private ResourceDelegate<ApplicationFormResource> applicationDelegate;

	private ResourceDelegate<CategoriesResource> categoriesDelegate;

	private ResourceDelegate<InvoiceResource> invoiceResource;

	@Inject
	public MemberRegistrationPresenter(final EventBus eventBus,
			final MyView view, final MyProxy proxy,
			Provider<ErrorPresenter> provider,
			ResourceDelegate<ApplicationFormResource> applicationDelegate,
			ResourceDelegate<UsersResource> usersDelegate,
			ResourceDelegate<CategoriesResource> categoriesDelegate,
			ResourceDelegate<InvoiceResource> invoiceResource) {
		super(eventBus, view, proxy);
		this.applicationDelegate = applicationDelegate;
		this.usersDelegate = usersDelegate;
		this.categoriesDelegate = categoriesDelegate;
		this.invoiceResource = invoiceResource;
		this.errorFactory = new StandardProvider<ErrorPresenter>(provider);
	}

	@Override
	protected void revealInParent() {
		RevealRootContentEvent.fire(this, this);
	}

	@Override
	protected void onBind() {
		super.onBind();
		addRegisteredHandler(ErrorEvent.TYPE, this);
		
		getView().getEmail().addValueChangeHandler(
				new ValueChangeHandler<String>() {

					@Override
					public void onValueChange(ValueChangeEvent<String> event) {
						String email = event.getValue();
						checkExists(email);
					}
				});

		getView().getANext().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (getView().isValid()) {
					if (getView().getCounter() == 1) {
						// User has selected a category and clicked submit
						submit(getView().getApplicationForm());
					} else {
						getView().next();
					}

				} else if (getView().getCounter() == 1) {
					getView().showError("Kindly select a category");
				}

			}
		});

		getView().getABack().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
			}
		});

	}
	
	@Override
	public void onError(final ErrorEvent event) {
		addToPopupSlot(null);
		errorFactory.get(new ServiceCallback<ErrorPresenter>() {
			@Override
			public void processResult(ErrorPresenter result) {
				String message = event.getMessage();
				
				result.setMessage(message, event.getId());
				
				MemberRegistrationPresenter.this.addToPopupSlot(result);
				
			}
		});
	}

	protected void submit(ApplicationFormHeaderDto applicationForm) {
		getView().setLoadingState((ActionLink) getView().getANext(), true);

		applicationDelegate.withCallback(
				new AbstractAsyncCallback<ApplicationFormHeaderDto>() {
					@Override
					public void onSuccess(ApplicationFormHeaderDto result) {
						removeError();
						// result;
						getView().bindForm(result);

						getView().getActivateAccLink().setHref(
								"#activateacc;uid=" + result.getUserRefId());
						getInvoice(result.getInvoiceRef());

					}

					private void removeError() {
						getView().setLoadingState(
								(ActionLink) getView().getANext(), false);
					}

					@Override
					public void onFailure(Throwable caught) {
						removeError();
						super.onFailure(caught);
					}
				}).create(applicationForm);

	}

	protected void getInvoice(String invoiceRef) {

		invoiceResource.withCallback(new AbstractAsyncCallback<InvoiceDto>() {
			@Override
			public void onSuccess(InvoiceDto invoice) {
				getView().bindInvoice(invoice);
				getView().next();
			}
		}).getInvoice(invoiceRef);

	}

	protected void checkExists(String email) {
		getView().setEmailValid(true);

		if (refid != null) {
			return;
		}

		usersDelegate.withCallback(new AbstractAsyncCallback<UserDto>() {

			@Override
			public void onSuccess(UserDto result) {
				getView().setEmailValid(false);
			}
		}).getById(email);
	}

	@Override
	public void prepareFromRequest(PlaceRequest request) {
		super.prepareFromRequest(request);
		categoriesDelegate.withCallback(
				new AbstractAsyncCallback<List<ApplicationCategoryDto>>() {
					@Override
					public void onSuccess(List<ApplicationCategoryDto> result) {
						getView().setCategories(result);
					}
				}).getAll();
	}

	@Override
	protected void onReset() {
		super.onReset();
		getView().setMiddleHeight();
	}

}
