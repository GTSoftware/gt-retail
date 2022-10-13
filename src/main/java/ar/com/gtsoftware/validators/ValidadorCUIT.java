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
package ar.com.gtsoftware.validators;

/**
 * @author Rodrigo Tato <rotatomel@gmail.com>
 */
public abstract class ValidadorCUIT {

  private static final String coef = "5432765432"; // coeficiente

  // método para determinar el CUIT
  public static boolean getValidate(String numCUIT) {
    try {
      int su = 0;
      int lCuit = numCUIT.length();
      if (lCuit < 11) {
        // numCUIT = "00000000000";
        return false;
      }
      for (int i = 1; i < 11; i++) {
        String Cd1 = coef.substring(i - 1, i);
        String Cd2 = numCUIT.substring(i - 1, i);
        int cf = Integer.parseInt(Cd1); // casteo...
        int ct = Integer.parseInt(Cd2); // casteo...
        su += (cf * ct);
      }
      int md = su / 11;
      int re = su - (md * 11);
      if (re > 1) {
        re = 11 - re;
      }
      String CdDv = numCUIT.substring(lCuit - 1, lCuit);
      int dv = Integer.parseInt(CdDv); // casteo...
      return dv == re;

    } catch (NumberFormatException e) {
      return false;
    }
  }
}
