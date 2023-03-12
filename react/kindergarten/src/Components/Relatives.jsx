import {useState, useEffect} from "react";
import RelativesService from "../API/RelativesService";

function Relatives({tokens, setTokens, kidId}) {
    const [relatives, setRelatives] = useState([]);

    useEffect(() => {
        async function fetchRelatives() {
            RelativesService.get(tokens, setTokens, kidId).then(data => {
                if (data.debugMessage) alert(data.debugMessage)
                else setRelatives(data);
            });

        }

        fetchRelatives();
    }, []);

    return (
        <div>
            <h1>Relatives:</h1>
            <table>
                <thead>
                <tr>
                    <th>Name</th>
                    <th>Phone</th>
                    <th>Address</th>
                </tr>
                </thead>
                <tbody>
                {relatives.map((relative) => (
                    <tr key={relative.id}>
                        <td>{relative.name}</td>
                        <td>{relative.phone}</td>
                        <td>{relative.address}</td>
                    </tr>
                ))}
                </tbody>
            </table>
        </div>
    );
}

export default Relatives;
