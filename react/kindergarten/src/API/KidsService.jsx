import CallApi from "./CallApi";

export default class KidsService {

    static async getAll(tokens, setTokens) {
        const requestOptions = {
            method: 'GET',
            mode: 'cors',
            headers: {
                'Authorization': 'Bearer ' + tokens.access_token
            }
        };
        return await CallApi.callApi('http://localhost:8080/kindergarten/child', requestOptions, tokens, setTokens);
    }

    static async getKidsThatWaitBirth(tokens, setTokens) {
        const requestOptions = {
            method: 'GET',
            mode: 'cors',
            headers: {
                'Authorization': 'Bearer ' + tokens.access_token
            }
        };
        return await CallApi.callApi('http://localhost:8080/kindergarten/child/birth', requestOptions, tokens, setTokens);
    }

    static async getKidsWithRelatives(tokens, setTokens) {
        const requestOptions = {
            method: 'GET',
            mode: 'cors',
            headers: {
                'Authorization': 'Bearer ' + tokens.access_token
            }
        };
        return await CallApi.callApi('http://localhost:8080/kindergarten/child/full', requestOptions, tokens, setTokens);
    }

    static async getBrothersAndSisters(id, tokens, setTokens) {
        const requestOptions = {
            method: 'GET',
            mode: 'cors',
            headers: {
                'Authorization': 'Bearer ' + tokens.access_token
            }
        };
        return await CallApi.callApi(`http://localhost:8080/kindergarten/child/${id}`, requestOptions, tokens, setTokens);
    }

    static async add(name, birthYear, tokens, setTokens) {
        const requestOptions = {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + tokens.access_token
            },
            body: JSON.stringify({name, birthYear})
        };
        return await CallApi.callApi('http://localhost:8080/kindergarten/child', requestOptions, tokens, setTokens);
    }

    static async update(id, name, birthYear, tokens, setTokens) {
        const requestOptions = {
            method: 'PATCH',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + tokens.access_token
            },
            body: JSON.stringify({id, name, birthYear})
        };
        return await CallApi.callApi(`http://localhost:8080/kindergarten/child`, requestOptions, tokens, setTokens);
    }

    static async delete(id, tokens, setTokens) {
        const requestOptions = {
            method: 'DELETE',
            mode: 'cors',
            headers: {
                'Authorization': 'Bearer ' + tokens.access_token
            }
        };
        return await CallApi.callApi(`http://localhost:8080/kindergarten/child/${id}`, requestOptions, tokens, setTokens);
    }
}