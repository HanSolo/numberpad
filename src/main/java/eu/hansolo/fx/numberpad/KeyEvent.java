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


public class KeyEvent {
    private final Key          source;
    private final KeyEventType type;


    // ******************** Constructors **************************************
    public KeyEvent(final Key source, final KeyEventType type) {
        this.source = source;
        this.type   = type;
    }


    // ******************** Methods *******************************************
    public Key getKey() { return source; }

    public KeyEventType getType() { return type; }
}

