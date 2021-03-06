/*
 * Copyright 2017 GT Software.
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
/**
 * Author:  Rodrigo
 * Created: Jun 4, 2019
 */
INSERT INTO parametros (nombre_parametro, valor_parametro, descripcion_parametro) VALUES
('presupuesto.impresion.mostrar_detalle_precios', '1', '0 = No mostrar sub totales ni precios unitarios en presupuesto. 1 = mostrar.');

DELETE FROM parametros WHERE nombre_parametro = 'presupuesto.impresion.cantidad_copias';
