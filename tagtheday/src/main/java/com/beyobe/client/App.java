/*
 * Copyright 2010 Daniel Kurka
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.beyobe.client;

import com.beyobe.client.views.TagdayView;
import com.beyobe.client.views.TagdayViewImpl;
import com.google.gwt.place.shared.PlaceController;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.SimpleEventBus;

/**
 * @author Daniel Kurka
 * 
 */
public class App {

	public static final EventBus eventBus = new SimpleEventBus();
	public static final PlaceController placeController = new PlaceController(eventBus);
	public static final TagdayView tagdayView = new TagdayViewImpl();
}
