/*
 * Copyright 2018 GT Software.
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
package ar.com.gtsoftware.dto.fiscal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * Clase para representar un ticket de autorización para los WS de AFIP
 *
 * @author Rodrigo M. Tato Rothamel mailto:rotatomel@gmail.com
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthTicket implements Serializable {

    private static final long serialVersionUID = 1L;

    private String token;
    private String sign;
    private Date expirationDate;

    @Override
    public String toString() {
        return String.format("AuthTicket{token=%s, sign=%s, expirationDate=%s}", token, sign, expirationDate);
    }

}
