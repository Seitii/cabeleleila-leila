import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { ClientesRoutingModule } from './clientes-routing.module';
import { ClientesFormComponent } from './clientes-form/clientes-form.component';


@NgModule({
  declarations: [ClientesFormComponent],
  imports: [
    CommonModule,
    ClientesRoutingModule  // conhece a rota de clientes-form
  ],
  exports: [ClientesFormComponent]
})
export class ClientesModule { }
