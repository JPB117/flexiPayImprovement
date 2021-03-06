package com.workpoint.icpak.client.ui.upload.attachment;

import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;
import com.workpoint.icpak.client.model.UploadContext;
import com.workpoint.icpak.client.ui.images.ImageResources;
import com.workpoint.icpak.client.ui.upload.attachment.AttachmentPresenter.IAttachmentView;

public class AttachmentView extends ViewImpl implements IAttachmentView{

	private final Widget widget;
	
	public interface Binder extends UiBinder<Widget, AttachmentView>{
		
	}
	
	@UiField Anchor aDownload;
	@UiField SpanElement spnName;
	@UiField SpanElement spnSize;
	@UiField HTMLPanel panelImg;
	
	String url = "";
	@Inject
	public AttachmentView(Binder binder){
		widget = binder.createAndBindUi(this);	
		aDownload.removeStyleName("gwt-Anchor");
		aDownload.getElement().getStyle().setDisplay(Display.INLINE);
//		aDownload.addClickHandler(new ClickHandler() {
//			@Override
//			public void onClick(ClickEvent event) {
//				String moduleUrl = GWT.getModuleBaseURL().replace("/gwtht", "");
//				if(moduleUrl.endsWith("/")){
//					moduleUrl = moduleUrl.substring(0, moduleUrl.length()-1);
//				}
//				url = url.replace("/", "");
//				moduleUrl =moduleUrl+"/"+url;
//				Window.open(moduleUrl, spnName.getInnerText(), "");
//			}
//		});
		
	}
	
	@Override
	public Widget asWidget() {
		return widget;
	}

	@Override
	public void setValues(long id,String name, String size) {
		String shortName;
		if(name.length()>28){
			shortName = name.substring(0, 28) + "...";
		}else{
			shortName=name;
		}
		spnName.setInnerText(shortName);
		spnName.setTitle(name);
		spnSize.setInnerText(""+size);
		
		UploadContext context = new UploadContext("getreport");
		context.setContext("attachmentId", id+"");
		context.setContext("ACTION", "GETATTACHMENT");
		url = context.toUrl();
		
		Image img = null;
		if(name.endsWith(".pdf")){
			img = new Image(ImageResources.IMAGES.pdf());
		}else if(name.endsWith(".docx") || name.endsWith(".doc")){
			img = new Image(ImageResources.IMAGES.doc());
		}else if(name.endsWith(".odt")){
			img = new Image(ImageResources.IMAGES.odt());
		}else if(name.endsWith(".ods")){
			img = new Image(ImageResources.IMAGES.ods());
		}else if(name.endsWith(".csv")){
			img = new Image(ImageResources.IMAGES.csv());
		}
		else if(name.endsWith(".xls") || name.endsWith(".xlsx")){
			img = new Image(ImageResources.IMAGES.xls());
		}
		else if(name.endsWith(".png") || name.endsWith(".jpg") 
				|| name.endsWith(".jpeg") ||name.endsWith(".gif")){
			img = new Image(ImageResources.IMAGES.img());
		}else if(name.endsWith(".txt") ||name.endsWith(".text")){
			img = new Image(ImageResources.IMAGES.txt());
		}
		else{
			img = new Image(ImageResources.IMAGES.file());
		}
		
		panelImg.add(img);
	}
	
	public HasClickHandlers getDownloadLink(){
		return aDownload;				
	}

}
