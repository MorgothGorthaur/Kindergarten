import CallApi from "./CallApi";

export default class AdminService {
    static async save(email, password, phone) {
        try {
            const requestOptions = {
                method: 'POST',
                headers: {'Content-Type': 'application/json'},
                body: JSON.stringify({email, password, phone})
            };
            const response = await fetch('http://localhost:8080/kindergarten/admin', requestOptions);
            const data = await response.json();
            if (data.debugMessage) alert(data.debugMessage);
        } catch (e) {
            console.log(e);
        }
        window.location.reload(false);
    };

    static async getAdmin(access_token) {
        try {
            const response = await fetch('http://localhost:8080/kindergarten/admin', {
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
            const data = await CallApi.callApi('http://localhost:8080/kindergarten/admin', requestOptions, tokens, setTokens);
            if (data) alert(data.debugMessage)
            else window.location.reload(false);
        } catch (e) {
            console.log(e);
        }
    };

    static async change(email, password, phone, tokens, setTokens) {
        try {
            const requestOptions = {
                method: 'PATCH',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': 'Bearer ' + tokens.access_token
                },
                body: JSON.stringify({email, password, phone})
            };
            const data = await CallApi.callApi('http://localhost:8080/kindergarten/admin', requestOptions, tokens, setTokens);
            if (data) alert(data.debugMessage);
            else window.location.reload(false);
        } catch (e) {
            console.log(e);
        }
    };
}