/*
 * Copyright (c) 2020 by Gerrit Grunwald
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package eu.hansolo.fx.numberpad;

import javafx.application.ConditionalFeature;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.StringProperty;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.VPos;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TouchEvent;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

import java.util.concurrent.ConcurrentHashMap;


public class Key<T> extends Region {
    private static final String                                            CSS_FILE         = "key.css";
    private static final double                                            PREFERRED_WIDTH  = 128;
    private static final double                                            PREFERRED_HEIGHT = 128;
    private static final double                                            MINIMUM_WIDTH    = 16;
    private static final double                                            MINIMUM_HEIGHT   = 16;
    private static final double                                            MAXIMUM_WIDTH    = 1024;
    private static final double                                            MAXIMUM_HEIGHT   = 1024;
    private              String                                            userAgentStyleSheet;
    private              ConcurrentHashMap<KeyEventObserver, KeyEventType> observers;
    private              Text                                              text;
    private              StackPane                                         pane;
    private              double                                            size;
    private              double                                            width;
    private              double                                            height;
    private              String                                            keyText;
    private              T                                                 _metaData;
    private              ObjectProperty<T>                                 metaData;
    private              EventHandler<MouseEvent>                          mouseHandler;
    private              EventHandler<TouchEvent>                          touchHandler;


    // ******************** Constructors **************************************
    public Key() {
        this(null, null);
    }
    public Key(final String keyText) {
        this(keyText, null);
    }
    public Key(final String keyText, final T metaData) {
        this.observers    = new ConcurrentHashMap<>();
        this.keyText      = null == keyText ? "" : keyText;
        this._metaData    = metaData;
        this.mouseHandler = e -> {
            EventType<? extends MouseEvent> type = e.getEventType();
            if (MouseEvent.MOUSE_PRESSED.equals(type)) {
                fireKeyEvent(new KeyEvent(Key.this, KeyEventType.PRESSED));
            } else if (MouseEvent.MOUSE_RELEASED.equals(type)) {
                fireKeyEvent(new KeyEvent(Key.this, KeyEventType.RELEASED));
            }
        };
        this.touchHandler = e -> {
            EventType<? extends TouchEvent> type  = e.getEventType();
            if (TouchEvent.TOUCH_PRESSED.equals(type)) {
                fireKeyEvent(new KeyEvent(Key.this, KeyEventType.PRESSED));
            } else if (TouchEvent.TOUCH_RELEASED.equals(type)) {
                fireKeyEvent(new KeyEvent(Key.this, KeyEventType.RELEASED));
            }
        };
        initGraphics();
        registerListeners();
    }


    // ******************** Initialization ************************************
    private void initGraphics() {
        if (Double.compare(getPrefWidth(), 0.0) <= 0 || Double.compare(getPrefHeight(), 0.0) <= 0 || Double.compare(getWidth(), 0.0) <= 0 ||
            Double.compare(getHeight(), 0.0) <= 0) {
            if (getPrefWidth() > 0 && getPrefHeight() > 0) {
                setPrefSize(getPrefWidth(), getPrefHeight());
            } else {
                setPrefSize(PREFERRED_WIDTH, PREFERRED_HEIGHT);
            }
        }

        text = new Text(keyText);
        text.setTextOrigin(VPos.CENTER);
        text.getStyleClass().add("text");

        pane = new StackPane(text);
        pane.getStyleClass().add("key");

        getChildren().setAll(pane);
    }

    private void registerListeners() {
        widthProperty().addListener(o -> resize());
        heightProperty().addListener(o -> resize());
        if (Platform.isSupported(ConditionalFeature.INPUT_MULTITOUCH)) {
            addEventHandler(TouchEvent.TOUCH_PRESSED, touchHandler);
            addEventHandler(TouchEvent.TOUCH_RELEASED, touchHandler);
        } else {
            addEventHandler(MouseEvent.MOUSE_PRESSED, mouseHandler);
            addEventHandler(MouseEvent.MOUSE_RELEASED, mouseHandler);
        }
    }


    // ******************** Methods *******************************************
    @Override protected double computeMinWidth(final double HEIGHT)  { return MINIMUM_WIDTH; }
    @Override protected double computeMinHeight(final double WIDTH)  { return MINIMUM_HEIGHT; }
    @Override protected double computePrefWidth(final double HEIGHT) { return super.computePrefWidth(HEIGHT); }
    @Override protected double computePrefHeight(final double WIDTH) { return super.computePrefHeight(WIDTH); }
    @Override protected double computeMaxWidth(final double HEIGHT)  { return MAXIMUM_WIDTH; }
    @Override protected double computeMaxHeight(final double WIDTH)  { return MAXIMUM_HEIGHT; }

    public String getKeyText() { return text.getText(); }
    public void setText(final String text) { this.text.setText(text); }
    public StringProperty textProperty() { return text.textProperty(); }

    public T getMetaData() { return null == metaData ? _metaData : metaData.get(); }
    public void setMetaData(final T metaData) {
        if (null == this.metaData) {
            _metaData = metaData;
        } else {
            this.metaData.set(metaData);
        }
    }
    public ObjectProperty<T> metaDataProperty() {
        if (null == metaData) {
            metaData = new ObjectPropertyBase<>(_metaData) {
                @Override public Object getBean() { return Key.this; }
                @Override public String getName() { return "metaData"; }
            };
            _metaData = null;
        }
        return metaData;
    }

    private void adjustTextSize() {
        if (getKeyText().length() > 3) {
            double charWidth = width * 0.9 / getKeyText().length();
            text.setFont(Fonts.robotoMonoRegular(charWidth * 1.66639544344996));
        } else {
            text.setFont(Fonts.robotoMonoRegular(size * 0.5));
        }
    }


    // ******************** EventHandling *************************************
    public void setOnKeyPressed(final KeyEventObserver observer) {
        if (!observers.containsKey(observer)) { observers.put(observer, KeyEventType.PRESSED); }
    }
    public void removeOnKeyPressed(final KeyEventObserver observer) { removeObserver(observer); }
    public void setOnKeyReleased(final KeyEventObserver observer) {
        if (!observers.containsKey(observer)) { observers.put(observer, KeyEventType.RELEASED); }
    }
    public void removeOnKeyReleased(final KeyEventObserver observer) { removeObserver(observer); }

    public void removeAllObservers() { observers.clear(); }
    private void removeObserver(final KeyEventObserver observer) {
        if (observers.containsKey(observer)) { observers.remove(observer); }
    }

    private void fireKeyEvent(final KeyEvent evt) {
        observers.entrySet().stream().filter(o -> o.getValue() == evt.getType()).forEach(entry -> entry.getKey().onKeyEvent(evt));
    }


    // ******************** Resizing ******************************************
    private void resize() {
        width  = getWidth() - getInsets().getLeft() - getInsets().getRight();
        height = getHeight() - getInsets().getTop() - getInsets().getBottom();
        size   = width < height ? width : height;

        if (width > 0 && height > 0) {
            pane.setMinSize(width, height);
            pane.setMaxSize(width, height);
            pane.setPrefSize(width, height);
            pane.relocate((getWidth() - width) * 0.5, (getHeight() - height) * 0.5);

            adjustTextSize();
        }
    }


    // ******************** Style related *************************************
    @Override public String getUserAgentStylesheet() {
        if (null == userAgentStyleSheet) { userAgentStyleSheet = Key.class.getResource(CSS_FILE).toExternalForm(); }
        return userAgentStyleSheet;
    }
}
