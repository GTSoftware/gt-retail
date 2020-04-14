/*
 * Copyright 2014 GT Software.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ar.com.gtsoftware.domain;

/**
 *
 * @author Rodrigo Tato <rotatomel@gmail.com>
 */
public abstract class BaseEntity extends GTEntity<Long> {

    @Override
    public boolean isNew() {
        return getId() == null;
    }

    @Override
    public String getStringId() {
        if (getId() != null) {
            return getId().toString();
        }
        return null;
    }

    @Override
    public Long calculateId(String id) {
        if (id != null) {
            try {
                return Long.parseLong(id);
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }

}
