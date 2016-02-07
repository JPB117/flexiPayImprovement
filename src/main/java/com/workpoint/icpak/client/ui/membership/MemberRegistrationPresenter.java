package com.workpoint.icpak.client.ui.membership;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.Window;
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
import com.workpoint.icpak.client.ui.events.PaymentCompletedEvent;
import com.workpoint.icpak.client.ui.events.PaymentCompletedEvent.PaymentCompletedHandler;
import com.workpoint.icpak.client.ui.events.ProcessingCompletedEvent;
import com.workpoint.icpak.client.ui.events.ProcessingCompletedEvent.ProcessingCompletedHandler;
import com.workpoint.icpak.client.ui.events.ProcessingEvent;
import com.workpoint.icpak.client.ui.events.ProcessingEvent.ProcessingHandler;
import com.workpoint.icpak.client.ui.membership.form.MemberRegistrationForm;
import com.workpoint.icpak.client.ui.payment.PaymentPresenter;
import com.workpoint.icpak.shared.api.ApplicationFormResource;
import com.workpoint.icpak.shared.api.CategoriesResource;
import com.workpoint.icpak.shared.api.CountriesResource;
import com.workpoint.icpak.shared.api.InvoiceResource;
import com.workpoint.icpak.shared.api.UsersResource;
import com.workpoint.icpak.shared.model.ApplicationCategoryDto;
import com.workpoint.icpak.shared.model.ApplicationFormHeaderDto;
import com.workpoint.icpak.shared.model.Country;
import com.workpoint.icpak.shared.model.InvoiceDto;
import com.workpoint.icpak.shared.model.UserDto;

public class MemberRegistrationPresenter
		extends
		Presenter<MemberRegistrationPresenter.MyView, MemberRegistrationPresenter.MyProxy>
		implements ErrorHandler, PaymentCompletedHandler, ProcessingHandler,
		ProcessingCompletedHandler {

	public interface MyView extends View {
		ApplicationFormHeaderDto getApplicationForm();

		ActionLink getANext();

		Anchor getABack();

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

		void showmask(boolean processing);

		void setCounter(int counter);

		void bindTransaction(InvoiceDto invoice);

		void setCountries(List<Country> countries);

		MemberRegistrationForm getRegistrationForm();
	}

	@ProxyCodeSplit
	@NameToken(NameTokens.signup)
	@NoGatekeeper
	public interface MyProxy extends ProxyPlace<MemberRegistrationPresenter> {
	}

	@ContentSlot
	public static final Type<RevealContentHandler<?>> PAYMENTS_SLOT = new Type<RevealContentHandler<?>>();

	@Inject
	PlaceManager placeManager;
	@Inject
	PaymentPresenter paymentPresenter;

	IndirectProvider<ErrorPresenter> errorFactory;

	private String applicationRefId = null;

	private ResourceDelegate<UsersResource> usersDelegate;

	private ResourceDelegate<ApplicationFormResource> applicationDelegate;

	private ResourceDelegate<CategoriesResource> categoriesDelegate;

	private ResourceDelegate<InvoiceResource> invoiceResource;

	private InvoiceDto invoice;

	private ResourceDelegate<CountriesResource> countriesResource;

	@Inject
	public MemberRegistrationPresenter(final EventBus eventBus,
			final MyView view, final MyProxy proxy,
			Provider<ErrorPresenter> provider,
			ResourceDelegate<ApplicationFormResource> applicationDelegate,
			ResourceDelegate<UsersResource> usersDelegate,
			ResourceDelegate<CategoriesResource> categoriesDelegate,
			ResourceDelegate<InvoiceResource> invoiceResource,
			ResourceDelegate<CountriesResource> countriesResource) {
		super(eventBus, view, proxy);
		this.applicationDelegate = applicationDelegate;
		this.usersDelegate = usersDelegate;
		this.categoriesDelegate = categoriesDelegate;
		this.invoiceResource = invoiceResource;
		this.countriesResource = countriesResource;
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
		addRegisteredHandler(ProcessingEvent.TYPE, this);
		addRegisteredHandler(ProcessingCompletedEvent.TYPE, this);
		addRegisteredHandler(PaymentCompletedEvent.TYPE, this);

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
						submit(getView().getApplicationForm());
					} else if (getView().getCounter() == 2) {
						if (invoice != null) {
							getView().bindTransaction(invoice);
							paymentPresenter.bindTransaction(invoice);
							getView().next();
						} else {
							Window.alert("Invoice details are null!");
						}
					} else if (getView().getCounter() == 3) {
						// Activating account
						getView().getANext().setHref(
								"#activateacc;uid="
										+ applicationDetails.getUserRefId());
					}

					else {
						getView().next();
					}

				} else if (getView().getCounter() == 1) {
					getView().showError("Kindly select a category");
				} else {
					// Window.alert("Either the form is not valid or app refId is null");
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

	private ApplicationFormHeaderDto applicationDetails;

	protected void submit(ApplicationFormHeaderDto applicationForm) {
		getView().setLoadingState((ActionLink) getView().getANext(), true);
		getView().showmask(true);

		AbstractAsyncCallback<ApplicationFormHeaderDto> callback = new AbstractAsyncCallback<ApplicationFormHeaderDto>() {
			@Override
			public void onSuccess(ApplicationFormHeaderDto result) {
				MemberRegistrationPresenter.this.applicationDetails = result;
				getView().showmask(false);
				removeError();
				getView().bindForm(result);
				getView().getActivateAccLink().setHref(
						"#activateacc;uid=" + result.getUserRefId());
				getView().getABack().addStyleName("hide");
				getView().getANext().addStyleName("hide");
				// This section removed on 5/12/2015
				// User Only pays once all documents have been submitted
				// getInvoice(result.getInvoiceRef());
				getView().next();

			}

			private void removeError() {
				getView().setLoadingState((ActionLink) getView().getANext(),
						false);
			}

			@Override
			public void onFailure(Throwable caught) {
				getView().showmask(false);
				Window.alert("There was a problem when submitting your data. Contact Us for more details..");
				removeError();
				super.onFailure(caught);
			}
		};

		if (applicationRefId == null) {
			applicationDelegate.withCallback(callback).create(applicationForm);
		} else {
			applicationDelegate.withCallback(callback).update(applicationRefId,
					applicationForm);
		}

	}

	protected void getInvoice(String invoiceRef) {
		invoiceResource.withCallback(new AbstractAsyncCallback<InvoiceDto>() {
			@Override
			public void onSuccess(InvoiceDto invoice) {
				MemberRegistrationPresenter.this.invoice = invoice;
				getView().bindInvoice(invoice);
			}
		}).getInvoice(invoiceRef);

	}

	protected void checkExists(String email) {
		getView().setEmailValid(true);

		if (applicationRefId != null) {
			return;
		}

		usersDelegate.withCallback(new AbstractAsyncCallback<UserDto>() {

			@Override
			public void onSuccess(UserDto result) {
				getView().setEmailValid(false);
			}

			@Override
			public boolean handleCustomError(Response aResponse) {
				int code = aResponse.getStatusCode();
				String message = aResponse.getText();
				if (code == 404) {
					getView().setEmailValid(true); // The email address does not
													// exist in our database as
													// expected
					return false;
				}

				/*
				 * Something went wrong on the server side while checking if the
				 * email exists esp. duplicate entries errors
				 * (NonUniqueResultExceptions)
				 */

				/*
				 * Do not allow the user to continue lest they input duplicate
				 * records
				 */
				getView().setEmailValid(false);

				/*
				 * Add an error message
				 */
				getView().getRegistrationForm().addError(message);
				return false;
			}
		}).getById(email);
	}

	@Override
	public void prepareFromRequest(PlaceRequest request) {
		super.prepareFromRequest(request);

		applicationRefId = request.getParameter("applicationId", null);

		// Countries
		countriesResource.withCallback(
				new AbstractAsyncCallback<List<Country>>() {
					public void onSuccess(List<Country> countries) {
						Collections.sort(countries, new Comparator<Country>() {
							@Override
							public int compare(Country o1, Country o2) {
								return o1.getDisplayName().compareTo(
										o2.getDisplayName());
							}
						});

						getView().setCountries(countries);
					};
				}).getAll();

		if (applicationRefId != null) {
			getView().next();
			getView().next();
			getView().setCounter(2);
			loadApplication(applicationRefId);
		}

		categoriesDelegate.withCallback(
				new AbstractAsyncCallback<List<ApplicationCategoryDto>>() {
					@Override
					public void onSuccess(List<ApplicationCategoryDto> result) {
						getView().setCategories(result);
					}
				}).getAll();
	}

	private void loadApplication(String applicationId) {

		// this.re
		applicationDelegate.withCallback(
				new AbstractAsyncCallback<ApplicationFormHeaderDto>() {
					@Override
					public void onSuccess(ApplicationFormHeaderDto dto) {
						getView().bindForm(dto);
						getInvoice(dto.getInvoiceRef());
					}
				}).getById(applicationId);
	}

	@Override
	protected void onReset() {
		super.onReset();
		getView().setMiddleHeight();
		setInSlot(PAYMENTS_SLOT, paymentPresenter);
	}

	@Override
	public void onPaymentCompleted(PaymentCompletedEvent event) {
		getView().getANext().setHref("#login");
	}

	@Override
	public void onProcessingCompleted(ProcessingCompletedEvent event) {
		getView().showmask(false);
	}

	@Override
	public void onProcessing(ProcessingEvent event) {
		getView().showmask(true);
	}
}