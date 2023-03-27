import CallApi from "./CallApi";

export default class TeacherService {
    static async getAll() {
        try {
            const response = await fetch('http://localhost:8080/kindergarten/teacher/all');
            return await response.json();
        } catch (e) {
            console.log(e);
        }
    };

    static async save(name, phone, skype, email, password) {
        try {
            const requestOptions = {
                method: 'POST',
                headers: {'Content-Type': 'application/json'},
                body: JSON.stringify({name, phone, skype, email, password})
            };
            const response = await fetch('http://localhost:8080/kindergarten/teacher', requestOptions);
            const data = await response.json();
            if (data.debugMessage) alert(data.debugMessage);
        } catch (e) {
            console.log(e);
        }
        window.location.reload(false);
    };

    static async getTeacher(access_token) {
        try {
            const response = await fetch('http://localhost:8080/kindergarten/teacher', {
                method: 'GET',
                mode: 'cors',
                headers: {
                    'Authorization': 'Bearer ' + access_token
                }
            });
            const data = await response.json();
            if (data.debugMessage) alert(data.debugMessage)
            else return data;
        } catch (e) {
            alert(e);
        }
    };

    static async delete(tokens, setTokens) {
        try {
            const requestOptions = {
                method: 'DELETE',
                mode: 'cors',
                headers: {
                    'Authorization': 'Bearer ' + tokens.access_token
                }
            };
            const data = await CallApi.callApi('http://localhost:8080/kindergarten/teacher', requestOptions, tokens, setTokens);
            if (data) alert(data.debugMessage)
            else window.location.reload(false);
        } catch (e) {
            console.log(e);
        }
    };

    static async change(name, phone, skype, email, password, tokens, setTokens) {
        try {
            const requestOptions = {
                method: 'PATCH',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': 'Bearer ' + tokens.access_token
                },
                body: JSON.stringify({name, phone, skype, email, password})
            };
            const data = await CallApi.callApi('http://localhost:8080/kindergarten/teacher', requestOptions, tokens, setTokens);
            if (data.debugMessage) alert(data.debugMessage);
            else window.location.reload(false);
        } catch (e) {
            console.log(e);
        }
    };
}