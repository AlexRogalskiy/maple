/**
 * Copyright 2019 Jordan Zimmerman
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.soabase.maple.core;

import io.soabase.maple.api.NameValue;
import io.soabase.maple.api.Names;
import io.soabase.maple.api.NamesValues;
import io.soabase.maple.api.Specialization;

import java.util.Iterator;
import java.util.Set;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

class NamesValuesImp implements NamesValues {
    private final Names names;
    private final Object[] arguments;

    NamesValuesImp(Names names, Object[] arguments) {
        this.names = names;
        this.arguments = arguments;
    }

    @Override
    public int qty() {
        return names.qty();
    }

    @Override
    public String nthName(int n) {
        return names.nthName(n);
    }

    @Override
    public String nthRawName(int n) {
        return names.nthRawName(n);
    }

    @Override
    public Object nthValue(int n) {
        return arguments[n];
    }

    @Override
    public Set<Specialization> nthSpecializations(int n) {
        return names.nthSpecializations(n);
    }

    @Override
    public Stream<NameValue> stream() {
        return newStream(this);
    }

    static Stream<NameValue> newStream(NamesValues namesValues) {
        Iterator<NameValue> iterator = new Iterator<NameValue>() {
            private int index = -1;

            @Override
            public boolean hasNext() {
                return (index + 1) < namesValues.qty();
            }

            @Override
            public NameValue next() {
                int thisIndex = ++index;
                return new NameValue() {
                    @Override
                    public String name() {
                        return namesValues.nthName(thisIndex);
                    }

                    @Override
                    public Object value() {
                        return namesValues.nthValue(thisIndex);
                    }
                };
            }
        };
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(iterator, Spliterator.ORDERED), false);
    }
}
