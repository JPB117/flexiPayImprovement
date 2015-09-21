package com.workpoint.icpak.client.ui.component;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.SelectionModel;
import com.workpoint.icpak.shared.model.HasKey;

public class Grid<T extends HasKey> extends Composite {

	private static GridUiBinder uiBinder = GWT.create(GridUiBinder.class);

	interface GridUiBinder extends UiBinder<Widget, Grid> {
	}

	@UiField HasWidgets container;
	@UiField PagingPanel paginPanel;
	
	private DataGrid<T> dataGrid;
	private ListDataProvider<T> dataProvider;
	private ListHandler<T> sortDataHandler;
	private final ProvidesKey<T> KEY_PROVIDER = new ProvidesKey<T>() {

		@Override
		public Object getKey(T item) {
			return item.getKey();
		}
	};

	private final SelectionModel<T> selectionModel = new MultiSelectionModel<T>(
			KEY_PROVIDER);

//	@UiField
//	SimplePanel gridPanel, pagerPanel;

	public Grid() {
		initWidget(uiBinder.createAndBindUi(this));
		initGrid();
	}
	
	public void initGrid(){
		dataGrid = createDataGrid();
		container.add(dataGrid);
	}

	private DataGrid<T> createDataGrid() {
		this.sortDataHandler = new ListHandler<T>(new ArrayList<T>());
		Column<T, Boolean> checkColumn = new Column<T, Boolean>(
				new CheckboxCell()) {
			@Override
			public Boolean getValue(T object) {
				boolean value = selectionModel.isSelected(object);

				return value;
			}
		};

		FieldUpdater<T, Boolean> checkColumnFU = new FieldUpdater<T, Boolean>() {

			@Override
			public void update(int index, T object, Boolean value) {
				selectionModel.setSelected(object, value);
			}
		};
		checkColumn.setFieldUpdater(checkColumnFU);

		final DataGrid<T> dataGrid = new DataGrid<T>(100, KEY_PROVIDER);
		dataGrid.setSize("100%", "75vh");
		//dataGrid.setHeight("200px");
		dataGrid.setStyleName("responsive-table");
		dataGrid.addColumn(checkColumn);
		dataGrid.setColumnWidth(checkColumn, "12px");

		SimplePager.Resources pagerResources = GWT
				.create(SimplePager.Resources.class);
		SimplePager pager = new SimplePager(TextLocation.CENTER,
				pagerResources, false, 0, true);
		pager.setDisplay(dataGrid);

		dataProvider = new ListDataProvider<T>();
		dataProvider.addDataDisplay(dataGrid);
		dataGrid.addColumnSortHandler(sortDataHandler);

		return dataGrid;

	}
	
	public void addColumn(Column col, String name){
		dataGrid.addColumn(col, name);
	}

	public void addColumn(Column col, String name, String width){
		addColumn(col, name);
		dataGrid.setColumnWidth(col, width);
	}

	public void setData(List<T> data) {
		dataProvider.setList(data);
		sortDataHandler.setList(data);
	}
	
	@Override
	protected void onLoad() {
		super.onLoad();
		dataGrid.onResize();
	}
	
	public PagingPanel getPagingPanel(){
		return paginPanel;
	}
		
}
