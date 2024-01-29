$(document).ready(function () {


    function fillTheTable() {
        fetch("/rest/all").then(response => response.json()).then(result => {
            for (let user of result) {
                let newTr = `<tr>
                    <td id="userID"> ${user.id} </td>
                    <td  id="userFirstName"> ${user.firstName} </td>  
                    <td  id="userLastName"> ${user.lastName} </td>  
                    <td id="userEmail"> ${user.email} </td> 
                     <td id="userAge"> ${user.age} </td> 
                      <td id="userPassword"> ${user.password} </td>                         
                    <td> 
                    <a  href="/rest/get?id=${user.id}" id="eBtn" class="btn btn-primary eBtn" style="color: white">
                    Edit
                    </a>
                    </td>
                    <td> 
                    <a  href="/rest/get?id=${user.id}" id= "dBtn" class="btn btn-danger dBtn">
                    Delete
                    </a> 
                    </td>"
                    </tr>`;
                $("#AllUsers > tbody").append(newTr);
            }
        })
    }

    fillTheTable();

    //================================================================
    //Edit form
    //================================================================
    $("#AllUsers > tbody").on("click", "#eBtn", function (event) {
        event.preventDefault();
        let href = $(this).attr("href");
        $.get(href, function (user, status) {
            $("#editId").val(user.id)
            $("#editFirstName").val(user.firstName)
            $("#editLastName").val(user.lastName)
            $("#editEmail").val(user.email)
            $("#editAge").val(user.age)
            $("#editPassword").val(user.password)
            $("#editRole").val(user.roles)

        });

        $("#Edit").on("click", "#saveChanges", async function (event) {
            event.preventDefault();
            let id = $("#editId").val();
            let firstName = $("#editFirstName").val();
            let lastName = $("#editLastName").val();
            let email = $("#editEmail").val();
            let age = $("#editAge").val();
            let password = $("#editPassword").val();


            let role = {
                role: $("#editRole").val()
            }

            let user = {
                "id": id,
                "firstName": firstName,
                "lastName": lastName,
                "email": email,
                "age": age,
                "password": password,
                "roles": [role]
            };

            await fetch('/rest/save', {
                method: 'post',
                headers: {
                    'Content-Type': 'application/json;charset=utf-8'
                },
                body: JSON.stringify(user)
            }).then(r => {
                if (r.status >= 400) {
                    alert("Error. Please try again")
                } else {
                    $("#AllUsers > tbody").html("");
                    fillTheTable();
                }

            });
            $(document.getElementById("Edit")).modal("hide");
            $("#Edit").off("click", "#saveChanges");

        });


        $(document.getElementById("Edit")).modal();

    });

    //================================================================
    //Delete form
    //================================================================
    $("#AllUsers > tbody").on("click", "#dBtn", function(event) {
        event.preventDefault();
        let href = $(this).attr("href");
        $.get(href, function (user, status) {
            $("#delId").val(user.id);
            $("#delFirstName").val(user.firstName);
            $("#delLastName").val(user.lastName);
            $("#delEmail").val(user.email);
            $("#delAge").val(user.age);
            $("#delPassword").val(user.password);
            $("#rolesString").val(user.roles);
        });

        $("#Delete").on("click", "#delButton", async function(event) {
            event.preventDefault();
            let id = $("#delId").val();
            console.log(id);
            await fetch("/rest/delete?id=" + id, {
            }).then(r =>{
                if(r.status >= 400){
                    alert("Error. Please try again")
                } else {
                    $("#AllUsers > tbody").html("");
                    fillTheTable();
                }
            })
            $(document.getElementById("Delete")).modal("hide");
            $("#Delete").off("click", "#delButton");
        });


        $(document.getElementById("Delete")).modal();
    });


    $("#addNewUser").on("click", async function (event) {
        event.preventDefault();
        let firstName = $("#newFirstName").val();
        let lastName = $("#newLastName").val();
        let email = $("#newEmail").val();
        let age = $("#newAge").val();
        let password = $("#newPassword").val();
        let role = {
            role: $("#newRole").val()
        };
        let user = {
            "firstName": firstName,
            "lastName": lastName,
            "email": email,
            "age": age,
            "password": password,
            "roles": [role]
        };


        await fetch("/rest/save", {
            method: 'post',
            headers: {
                'Content-Type': 'application/json;charset=utf-8'
            },
            body: JSON.stringify(user)
        }).then(r => {
            if (r.status >= 400) {
                alert("Error. Please try again")
            } else {
                $("#AllUsers > tbody").html("");
                fillTheTable();
            }
        });
    })
});