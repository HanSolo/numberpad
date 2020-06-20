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

import javafx.beans.DefaultProperty;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;

import java.util.ArrayList;
import java.util.List;


public class NumberPad extends Region {
    private static final String    CSS_FILE         = "numberpad.css";
    private static final double    PREFERRED_WIDTH  = 250;
    private static final double    PREFERRED_HEIGHT = 250;
    private static final double    MINIMUM_WIDTH    = 50;
    private static final double    MINIMUM_HEIGHT   = 50;
    private static final double    MAXIMUM_WIDTH    = 1024;
    private static final double    MAXIMUM_HEIGHT   = 1024;
    private              String    userAgentStyleSheet;
    private              double    width;
    private              double    height;
    private              double    horizontalGap;
    private              double    verticalGap;
    private              Key       key0;
    private              Key       key1;
    private              Key       key2;
    private              Key       key3;
    private              Key       key4;
    private              Key       key5;
    private              Key       key6;
    private              Key       key7;
    private              Key       key8;
    private              Key       key9;
    private              Key       keyDot;
    private              Key       keyPlus;
    private              Key       keyMinus;
    private              Key       keyDel;
    private              Key       keyBS;
    private              Key       keyUp;
    private              Key       keyRight;
    private              Key       keyDown;
    private              Key       keyLeft;
    private              Key       keyEnter;
    private              Key       keyCancel;
    private              Key       keyClr;
    private              List<Key> keys;
    private              GridPane  pane;


    // ******************** Constructors **************************************
    public NumberPad() {
        this(5, 5);
    }
    public NumberPad(final double horizontalGap, double verticalGap) {
        this.horizontalGap = horizontalGap;
        this.verticalGap   = verticalGap;
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

        getStyleClass().add("number-pad");

        keys = new ArrayList<>();

        key0     = createKey("0", KeyCode.NUMPAD0);
        key1     = createKey("1", KeyCode.NUMPAD1);
        key2     = createKey("2", KeyCode.NUMPAD2);
        key3     = createKey("3", KeyCode.NUMPAD3);
        key4     = createKey("4", KeyCode.NUMPAD4);
        key5     = createKey("5", KeyCode.NUMPAD5);
        key6     = createKey("6", KeyCode.NUMPAD6);
        key7     = createKey("7", KeyCode.NUMPAD7);
        key8     = createKey("8", KeyCode.NUMPAD8);
        key9     = createKey("9", KeyCode.NUMPAD9);
        keyDot   = createKey(".", KeyCode.SEPARATOR);
        keyMinus = createKey("-", KeyCode.MINUS);
        keyPlus  = createKey("+", KeyCode.PLUS);
        keyDel   = createKey("DEL", KeyCode.DELETE);
        keyBS    = createKey("BS", KeyCode.BACK_SPACE);
        keyUp     = createKey("\u25b2", KeyCode.UP);
        keyRight  = createKey("\u25b6", KeyCode.RIGHT);
        keyDown   = createKey("\u25bc", KeyCode.DOWN);
        keyLeft   = createKey("\u25c0", KeyCode.LEFT);
        keyEnter  = createKey("\u23ce", KeyCode.ENTER);
        keyCancel = createKey("CANCEL", KeyCode.CANCEL);
        keyClr    = createKey("CLR", KeyCode.CLEAR);

        pane = new GridPane();
        pane.setHgap(horizontalGap);
        pane.setVgap(verticalGap);
        pane.add(keyLeft, 0, 0);
        pane.add(keyRight, 1, 0);
        pane.add(keyClr, 2, 0);
        pane.add(keyCancel, 3, 0);
        pane.add(key7, 0, 1);
        pane.add(key8, 1, 1);
        pane.add(key9, 2, 1);
        pane.add(keyBS, 3, 1);
        pane.add(keyUp, 4, 1);
        pane.add(key4, 0, 2);
        pane.add(key5, 1, 2);
        pane.add(key6, 2, 2);
        pane.add(keyDel, 3, 2);
        pane.add(keyDown, 4, 2);
        pane.add(key1, 0, 3);
        pane.add(key2, 1, 3);
        pane.add(key3, 2, 3);
        pane.add(keyPlus, 3, 3);
        pane.add(keyEnter, 4, 3);
        pane.add(key0, 0, 4);
        pane.add(keyDot, 2, 4);
        pane.add(keyMinus, 3,4 );

        GridPane.setColumnSpan(key0, 2);
        GridPane.setColumnSpan(keyCancel, 2);
        GridPane.setRowSpan(keyEnter, 2);

        getChildren().setAll(pane);
    }

    private void registerListeners() {
        widthProperty().addListener(o -> resize());
        heightProperty().addListener(o -> resize());
    }


    // ******************** Methods *******************************************
    @Override protected double computeMinWidth(final double HEIGHT) { return MINIMUM_WIDTH; }
    @Override protected double computeMinHeight(final double WIDTH) { return MINIMUM_HEIGHT; }
    @Override protected double computePrefWidth(final double HEIGHT) { return super.computePrefWidth(HEIGHT); }
    @Override protected double computePrefHeight(final double WIDTH) { return super.computePrefHeight(WIDTH); }
    @Override protected double computeMaxWidth(final double HEIGHT) { return MAXIMUM_WIDTH; }
    @Override protected double computeMaxHeight(final double WIDTH) { return MAXIMUM_HEIGHT; }


    public void setOnKeyPressed(final KeyEventObserver observer) {
        keys.forEach(key -> key.setOnKeyPressed(observer));
    }
    public void removeOnKeyPressed(final KeyEventObserver observer) {
        keys.forEach(key -> key.removeOnKeyPressed(observer));
    }

    public void setOnKeyReleased(final KeyEventObserver observer) {
        keys.forEach(key -> key.setOnKeyReleased(observer));
    }
    public void removeOnKeyReleased(final KeyEventObserver observer) {
        keys.forEach(key -> key.removeOnKeyReleased(observer));
    }

    public double getHorizontalGap() { return horizontalGap; }
    public void setHorizontalGap(final double horizontalGap) {
        this.horizontalGap = horizontalGap;
        pane.setHgap(horizontalGap);
    }

    public double getVerticalGap() { return verticalGap; }
    public void setVerticalGap(final double verticalGap) {
        this.verticalGap = verticalGap;
        pane.setVgap(verticalGap);
    }

    private Key createKey(final String text) {
        return createKey(text,"number-pad", null);
    }
    private <T> Key createKey(final String text, final T metaData) { return createKey(text, "number-pad", metaData); }
    private <T> Key createKey(final String text, final String styleClass, final T metaData) {
        Key<T> key = new Key<>(text, metaData);
        if (null != styleClass && !styleClass.isEmpty()) { key.getStyleClass().add(styleClass); }
        GridPane.setHalignment(key, HPos.CENTER);
        GridPane.setValignment(key, VPos.CENTER);
        keys.add(key);
        return key;
    }


    // ******************** Resizing ******************************************
    private void resize() {
        width  = getWidth() - getInsets().getLeft() - getInsets().getRight();
        height = getHeight() - getInsets().getTop() - getInsets().getBottom();

        if (width > 0 && height > 0) {
            pane.setMaxSize(width, height);
            pane.setPrefSize(width, height);
            pane.relocate((getWidth() - width) * 0.5, (getHeight() - height) * 0.5);
        }
    }


    // ******************** Style related *************************************
    @Override public String getUserAgentStylesheet() {
        if (null == userAgentStyleSheet) { userAgentStyleSheet = Key.class.getResource(CSS_FILE).toExternalForm(); }
        return userAgentStyleSheet;
    }
}
