import CallApi from './CallApi'

export default class AllRelativesService {

    static async getAll(tokens, setTokens) {
        const requestOptions = {
            method: 'GET',
            mode: 'cors',
            headers: {
                'Authorization': 'Bearer ' + tokens.access_token
            }
        };
        return await CallApi.callApi('http://localhost:8080/kindergarten/admin/relatives', requestOptions, tokens, setTokens);
    }

    static async getAllSortedByName(tokens, setTokens) {
        const requestOptions = {
            method: 'GET',
            mode: 'cors',
            headers: {
                'Authorization': 'Bearer ' + tokens.access_token
            }
        };
        return await CallApi.callApi('http://localhost:8080/kindergarten/admin/relatives/by_name', requestOptions, tokens, setTokens);
    }

    static async getAllSortedByKidCount(tokens, setTokens) {
        const requestOptions = {
            method: 'GET',
            mode: 'cors',
            headers: {
                'Authorization': 'Bearer ' + tokens.access_token
            }
        };
        return await CallApi.callApi('http://localhost:8080/kindergarten/admin/relatives/by_kids', requestOptions, tokens, setTokens);
    }
}