import {useState, useEffect} from "react";
import KidsService from "../API/KidsService";

function KidBrothersAndSisters({id, tokens, setTokens}) {
    const [kids, setKids] = useState([]);

    useEffect(() => {
        const fetchKids = async () => {
            KidsService.getBrothersAndSisters(id, tokens, localStorage.setItem).then(data => {
                if (data.debugMessage) alert(data.debugMessage);
                else setKids(data);
            })
        };
        fetchKids();
    }, [id]);

    return (
        <div>
            <table>
                <thead>
                <tr>
                    <th>Name</th>
                    <th>Birth Year</th>
                    <th>Group name </th>
                </tr>
                </thead>
                <tbody>
                {kids.map((kid) => (
                    <tr key={kid.id}>
                        <td>{kid.name}</td>
                        <td>{kid.birthYear}</td>
                        <td>{kid.groupName}</td>
                    </tr>
                ))}
                </tbody>
            </table>
        </div>
    );
};

export default KidBrothersAndSisters;