import LoginService from "./LoginService";
import CallApi from "./CallApi";

export default class RelativesService {


    static async get(tokens, setTokens, id) {
        const requestOptions = {
            method: 'GET',
            mode: 'cors',
            headers: {
                'Authorization': 'Bearer ' + tokens.access_token
            }
        };
        return await CallApi.callApi(`http://localhost:8080/kindergarten/relative/${id}`, requestOptions, tokens, setTokens);
    }

    static async add(tokens, setTokens, kidId, name, phone, address) {
        const requestOptions = {
            method: 'POST',
            mode: 'cors',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + tokens.access_token
            },
            body: JSON.stringify({ name, phone, address})
        };
        return await CallApi.callApi(`http://localhost:8080/kindergarten/relative/${kidId}`, requestOptions, tokens, setTokens);
    }

    static async delete(tokens, setTokens, id, kidId) {
        const requestOptions = {
            method: 'DELETE',
            mode: 'cors',
            headers: {
                'Authorization': 'Bearer ' + tokens.access_token
            }
        };
        return await CallApi.callApi(`http://localhost:8080/kindergarten/relative/${kidId}/${id}`, requestOptions, tokens, setTokens);
    }

    static async update(tokens, setTokens, kidId, id, name, phone, address) {
        const requestOptions = {
            method: 'PATCH',
            mode: 'cors',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + tokens.access_token
            },
            body: JSON.stringify({ id, name, phone, address})
        };
        return await CallApi.callApi(`http://localhost:8080/kindergarten/relative/${kidId}`, requestOptions, tokens, setTokens);
    }
}