/*
 * Copyright 2012 Daniel Kurka
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.beyobe.client.widgets;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.logical.shared.HasSelectionHandlers;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;
import com.googlecode.mgwt.collection.shared.LightArrayInt;
import com.googlecode.mgwt.dom.client.event.orientation.OrientationChangeEvent;
import com.googlecode.mgwt.dom.client.event.orientation.OrientationChangeHandler;
import com.googlecode.mgwt.ui.client.MGWT;
import com.googlecode.mgwt.ui.client.MGWTStyle;
import com.googlecode.mgwt.ui.client.theme.base.CarouselCss;
import com.googlecode.mgwt.ui.client.widget.ScrollPanel;
import com.googlecode.mgwt.ui.client.widget.event.scroll.ScrollEndEvent;
import com.googlecode.mgwt.ui.client.widget.event.scroll.ScrollRefreshEvent;

/**
 * the carousel widget renders its children in a horizontal row. users can select a different child
 * by swiping between them
 * 
 * @author Daniel Kurka
 * 
 */
public class Carousel extends Composite implements HasWidgets, HasSelectionHandlers<Integer> {

  private static class WidgetHolder extends FlowPanel {

    public WidgetHolder(CarouselCss css) {
      addStyleName(css.carouselHolder());
    }

    @Override
    public void add(Widget w) {
      super.add(w);
      if (w instanceof ScrollPanel) {
        w.addStyleName(MGWTStyle.getTheme().getMGWTClientBundle().getLayoutCss().fillPanelExpandChild());
      }
    }

  }

  private FlowPanel main;
  private final CarouselCss css;
  private ScrollPanel scrollPanel;
  private FlowPanel container;

  private int currentPage;

  private Map<Widget, Widget> childToHolder;
  private com.google.web.bindery.event.shared.HandlerRegistration refreshHandler;

//  private static final CarouselImpl IMPL = GWT.create(CarouselImpl.class);
//  private static final CarouselImpl IMPL = new CarouselImplGecko();
  private static final CarouselImpl IMPL = new CarouselImplSafari();

  /**
   * Construct a carousel widget with the default css
   */
  public Carousel() {
    this(MGWTStyle.getTheme().getMGWTClientBundle().getCarouselCss());
  }

  /**
   * Construct a carousel widget with a given css
   * 
   * @param css the css to use
   */
  public Carousel(CarouselCss css) {
    this.css = css;
    this.css.ensureInjected();

    childToHolder = new HashMap<Widget, Widget>();
    main = new FlowPanel();
    initWidget(main);

    main.addStyleName(css.carousel());

    scrollPanel = new ScrollPanel();
    scrollPanel.addStyleName(css.carouselScroller());

    main.add(scrollPanel);

    container = new FlowPanel();
    container.addStyleName(css.carouselContainer());

    scrollPanel.setWidget(container);

    scrollPanel.setSnap(true);
    scrollPanel.setMomentum(true);
    scrollPanel.setShowScrollBarX(false);
    scrollPanel.setShowScrollBarY(false);
    scrollPanel.setScrollingEnabledX(true);
    scrollPanel.setScrollingEnabledY(false);
    scrollPanel.setAutoHandleResize(false);

    currentPage = 0;

    scrollPanel.addScrollEndHandler(new ScrollEndEvent.Handler() {

      @Override
      public void onScrollEnd(ScrollEndEvent event) {
        int page;

        page = scrollPanel.getCurrentPageX();

        currentPage = page;
        SelectionEvent.fire(Carousel.this, currentPage);

      }
    });

    MGWT.addOrientationChangeHandler(new OrientationChangeHandler() {

      @Override
      public void onOrientationChanged(OrientationChangeEvent event) {
        Scheduler.get().scheduleDeferred(new ScheduledCommand() {

          @Override
          public void execute() {
            refresh();

          }
        });

      }
    });

    addSelectionHandler(new SelectionHandler<Integer>() {

      @Override
      public void onSelection(SelectionEvent<Integer> event) {

      }
    });

    if (MGWT.getOsDetection().isDesktop()) {
      Window.addResizeHandler(new ResizeHandler() {

        @Override
        public void onResize(ResizeEvent event) {
          Scheduler.get().scheduleDeferred(new ScheduledCommand() {

            @Override
            public void execute() {
              refresh();

            }
          });

        }
      });
    }

  }

  public void addWidget(Widget w) {
  	ScrollPanel pageHolder = new ScrollPanel();
  	pageHolder.setWidth("90%");
  	pageHolder.setScrollingEnabledX(false);
  	pageHolder.setWidget(w);
//    pageHolder.getElement().getStyle().setProperty("float", "left");
    add(pageHolder);
//    if (w instanceof ScrollPanel) {
//      w.addStyleName(MGWTStyle.getTheme().getMGWTClientBundle().getLayoutCss().fillPanelExpandChild());
//    }
    //dd
//    w.getElement().getStyle().setProperty("float", "left");
//    w.addStyleName(MGWTStyle.getTheme().getMGWTClientBundle().getLayoutCss().fillPanelExpandChild());
  }
  
  @Override
  public void add(Widget w) {

    WidgetHolder widgetHolder = new WidgetHolder(css);
    widgetHolder.getElement().getStyle().setProperty("float", "left");
    widgetHolder.add(w);

    childToHolder.put(w, widgetHolder);

    container.add(widgetHolder);

  }

  @Override
  public void clear() {
    container.clear();
    childToHolder.clear();

  }

  @Override
  public Iterator<Widget> iterator() {
    Set<Widget> keySet = childToHolder.keySet();
    return keySet.iterator();
  }

  @Override
  public boolean remove(Widget w) {
    Widget holder = childToHolder.remove(w);
    if (holder != null) {
      return container.remove(holder);
    }
    return false;

  }

  @Override
  protected void onAttach() {
    super.onAttach();
    refresh();
  }

  /**
   * refresh the carousel widget, this is necessary after changing child elements
   */
  public void refresh() {

    IMPL.adjust(main, container);

    scrollPanel.setScrollingEnabledX(true);
    scrollPanel.setScrollingEnabledY(false);

    scrollPanel.setShowScrollBarX(false);
    scrollPanel.setShowScrollBarY(false);

    int widgetCount = container.getWidgetCount();

    if (currentPage >= widgetCount) {
      currentPage = widgetCount - 1;
    }

    scrollPanel.refresh();

    refreshHandler = scrollPanel.addScrollRefreshHandler(new ScrollRefreshEvent.Handler() {

      @Override
      public void onScrollRefresh(ScrollRefreshEvent event) {
        refreshHandler.removeHandler();
        refreshHandler = null;

        scrollPanel.scrollToPage(currentPage, 0, 0);

      }
    });

  }

  public void setSelectedPage(int index) {
    LightArrayInt pagesX = scrollPanel.getPagesX();
    if (index < 0 || index >= pagesX.length()) {
      throw new IllegalArgumentException("invalid value for index: " + index);
    }
    currentPage = index;
    scrollPanel.scrollToPage(index, 0, 300);
  }

  @Override
  public HandlerRegistration addSelectionHandler(SelectionHandler<Integer> handler) {
    return addHandler(handler, SelectionEvent.getType());
  }

  public ScrollPanel getScrollPanel() {
    return scrollPanel;
  }

  /**
   * 
   * @author Daniel Kurka
   * 
   */
  public static interface CarouselImpl {

    /**
     * 
     * @param main
     * @param container
     */
    void adjust(FlowPanel main, FlowPanel container);

  }

  /**
   * 
   * @author Daniel Kurka
   * 
   */
  public static class CarouselImplSafari implements CarouselImpl {

    @Override
    public void adjust(FlowPanel main, FlowPanel container) {
      int widgetCount = container.getWidgetCount();

      double sizeFactor = 100d / widgetCount;

      for (int i = 0; i < widgetCount; i++) {
        container.getWidget(i).setWidth(sizeFactor + "%");
      }

      container.setWidth((widgetCount * 100) + "%");

    }

  }

  /**
   * 
   * @author Daniel Kurka
   * 
   */
  public static class CarouselImplGecko implements CarouselImpl {

    @Override
    public void adjust(FlowPanel main, FlowPanel container) {
      int widgetCount = container.getWidgetCount();
      int offsetWidth = main.getOffsetWidth();

      container.setWidth(widgetCount * offsetWidth + "px");

      for (int i = 0; i < widgetCount; i++) {
        container.getWidget(i).setWidth(offsetWidth + "px");
      }

    }

  }
  
  public Widget getCurrentWidget() {
	  return container.getWidget(currentPage);
  }

}
