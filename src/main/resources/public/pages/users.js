$(document).ready(function() {
  $('#search-btn').click(function() {
    var id = $('#user-id').val();
    $.ajax({
      type: 'GET',
      url: '/api/user/' + id,
      dataType: 'json',
      success: function(response) {
      $("#search-results tr:not(:first)").remove();
         appendDataToTable(response);
         console.log(response);
         },
      error: function(jqXHR, textStatus, errorThrown) {
      $("#search-results tr:not(:first)").remove();
        if (jqXHR.status === 404) {
          $("#search-results").append("<tr><td colspan='8'>User was not found</td></tr>");
        } else {
        showError(jqXHR.responseJSON ? jqXHR.responseJSON.message: "An error occurred", jqXHR.responseText);
        }
        console.log(errorThrown);
       }
     });
   });

  function appendDataToTable(response) {
    var row = '<tr>' +
              '<td>' + response.userName + '</td>' +
              '<td>' + response.id + '</td>' +
              '<td>' + response.address + '</td>' +
              '<td>' + response.phone + '</td>' +
              '<td>' + response.email + '</td>' +
              '<td>' + response.login + '</td>' +
              '<td>' + response.roleName + '</td>' +
              '<td>' + response.roleId + '</td>' +
              '</tr>';
    $('#search-results').append(row);
     }
  });

 // click on Create button opens create user modal
  function openCreateModal() {
    fetch('user-modal.html')
      .then(response => response.text())
      .then(data => {
        // Create a new modal element
        const modal = document.createElement('div');
        modal.classList.add('modal');

        // Set the contents of the modal to the fetched HTML
        modal.innerHTML = data;

        // Append the modal to the page
        document.body.appendChild(modal);

        // Open the modal
        const modalElement = document.getElementById("create-modal");
        modalElement.style.display = 'block';
      });
  }

  function closeCreateModal() {
    var modal = document.getElementById("create-modal");
    clearModalFields(modal);
    modal.style.display = "none";
  }

  // When the user clicks on the save changes button in Create User modal, POST request is sent to create new User
    function createUser(event) {
    event.preventDefault();
    var userName = document.getElementById("user-name-modal").value;
    var roleId = document.getElementById('role-id-modal').value;
    var roleName = document.getElementById("role-name-modal").value;
    var email = document.getElementById("email-modal").value;
    var address = document.getElementById("address-modal").value;
    var phone = document.getElementById("phone-modal").value;
    var login = document.getElementById("login-modal").value;
    var password = document.getElementById("password-modal").value;
    var data = {
      userName: userName,
      roleId: roleId,
      roleName: roleName,
      email: email,
      address: address,
      phone: phone,
      login: login,
      password: password
    };

    fetch('/api/user', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(data)
    })
    .then(response => response.json())
    .then(data => {
      console.log('Success:', data);
      var modal = document.getElementById("create-modal");
      clearModalFields(modal);
      openPopUpWithUserId(data);
    })
    .catch((error) => {
      console.error('Error:', error);
    });
  }

  function openPopUpWithUserId(response) {
  // Fetch the HTML for the modal
  fetch('user-id-pop-up.html')
    .then(response => response.text())
    .then(data => {
      // Create a new modal element
      const modal = document.createElement('div');
      modal.classList.add('modal');

      // Set the contents of the modal to the fetched HTML
      modal.innerHTML = data;

      // Append the modal to the page
      document.body.appendChild(modal);

      // Update the modal HTML to include the user ID
      const modalContent = modal.querySelector('.modal-content');

      // Open the modal
      const modalElement = document.getElementById("user-id-pop-up-modal");
      var inputField = document.getElementById("user-id-pop-up");
      const { id } = response;
      inputField.value = id;
      modalElement.style.display = 'block';
    });
  }

  function clearModalFields(modal) {
      modal.querySelector("#user-name-modal").value = "";
      modal.querySelector("#email-modal").value = "";
      modal.querySelector("#address-modal").value = "";
      modal.querySelector("#phone-modal").value = "";
      modal.querySelector("#login-modal").value = "";
      modal.querySelector("#password-modal").value = "";
      modal.style.display = "none";
  }

  // When the user clicks on the Delete button, DELETE request is sent to delete User from Search Results
  function deleteUser() {
       var id = document.querySelector('#search-results tbody tr:last-child td:nth-of-type(2)').textContent;
       console.log("userId for delete: " + id);
       fetch('/api/user/' + id, {
         method: 'DELETE'
       })
       .then(response => {
         if(response.status === 204) {
         console.log('Success: User deleted successfully');
         // Reload the page
         location.reload();
         } else if (response.status === 404) {
         console.error('Error: User not found');
         } else {
         console.error('Error: ${response.status} ${response.statusText}')
         }
       })
       .catch(error => {
         console.error('Error: ', error);
       });
  }

  // When the user clicks on the Edit button, edit fields and press Save, PUT request is sent to update the User data
  function editUser() {
  // Open the edit modal
       fetch('edit-user-modal.html')
          .then(response => response.text())
          .then(data => {
            // Create a new modal element
            const modal = document.createElement('div');
            modal.classList.add('modal');

            // Set the contents of the modal to the fetched HTML
            modal.innerHTML = data;

            // Append the modal to the page
            document.body.appendChild(modal);

            const table = document.getElementById("search-results");
            const row = table.rows[1];
            updateModalFields(row);

            // Show the modal
            const modalElement = document.getElementById("edit-modal");
            modalElement.style.display = 'block';
        });
  }

  function updateModalFields(row) {
  // Extract values from the table row
    const userName = row.cells[0].textContent
    const userId = row.cells[1].textContent;
    const address = row.cells[2].textContent;
    const phone = row.cells[3].textContent;
    const email = row.cells[4].textContent;
    const login = row.cells[5].textContent;

  // Set values in the modal fields
    document.getElementById("user-id-modal").value = userId;
    document.getElementById("edit-user-name-modal").value = userName;
    document.getElementById("edit-email-modal").value = email;
    document.getElementById("edit-address-modal").value = address;
    document.getElementById("edit-phone-modal").value = phone;
    document.getElementById("edit-login-modal").value = login;
   }

   function closeEditModal() {
       var modal = document.getElementById("edit-modal");
       modal.style.display = "none";
   }

   function closePopUp() {
        var modal = document.getElementById("user-id-pop-up-modal");
        modal.style.display = "none";
   }

   // click on Save changes in Edit User modal send put request to User endpoint
   function saveUpdatedChanges(event) {
       event.preventDefault();

       //data population for PUT request
         var id = document.getElementById("user-id-modal").value;
         var userName = document.getElementById("edit-user-name-modal").value;
         var userRoleId = document.querySelector('#search-results tbody tr:last-child td:nth-of-type(8)').textContent;
         var email = document.getElementById("edit-email-modal").value;
         var address = document.getElementById("edit-address-modal").value;
         var phone = document.getElementById("edit-phone-modal").value;
         var login = document.getElementById("edit-login-modal").value;
         var password = document.getElementById("edit-password-modal").value;
         var data = {
           userName: userName,
           email: email,
           roleId: userRoleId,
           address: address,
           phone: phone,
           login: login,
           password: password
         };

        fetch('/api/user/' + id, {
          method: 'PUT',
          headers: {
         'Content-Type': 'application/json'
          },
          body: JSON.stringify(data)
        })
        .then(response => {
           if(response.status === 200) {
           console.log('User data has been successfully updated: ', data)
           closeEditModal();
           // page is reloaded without previous search results that have been updated
           location.reload();
           }
       })
       .catch((error) => {
         console.error('Error:', error);
       });
   }