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

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;


/**
 * User: hansolo
 * Date: 18.06.20
 * Time: 10:21
 */
public class Demo extends Application {
    private NumberPad numberPad;

    @Override public void init() {
        numberPad = new NumberPad(5, 5);

        KeyEventObserver pressedObserver  = evt -> System.out.println(evt.getKey().getMetaData() + " pressed");
        KeyEventObserver releasedObserver = evt -> System.out.println(evt.getKey().getMetaData() + " released");

        numberPad.setOnKeyPressed(pressedObserver);
        numberPad.setOnKeyReleased(releasedObserver);
    }

    @Override public void start(Stage stage) {
        StackPane pane = new StackPane(numberPad);

        Scene scene = new Scene(pane);

        stage.setTitle("NumberPad");
        stage.setScene(scene);
        stage.show();
    }

    @Override public void stop() {
        System.exit(0);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
