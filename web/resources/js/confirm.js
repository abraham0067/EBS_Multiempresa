/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
void function confirmTest(){
    bootbox.confirm("Are you sure?", function(result) {
  Example.show("Confirm result: "+result);
}); 
}

