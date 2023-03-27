import React, {useEffect, useState} from "react";
import KidsService from "../API/KidsService";
import Loader from "../UI/Loader/Loader";

function Siblings({id, tokens, setTokens}) {
    const [kids, setKids] = useState([]);
    const [loader, setLoader] = useState(true);
    useEffect(() => {
        const fetchKids = async () => {
            KidsService.getBrothersAndSisters(id, tokens, localStorage.setItem).then(data => {
                setKids(data);
                setLoader(false);
            })
        };
        fetchKids();
    }, [id]);

    return (
        <div style={{alignItems: 'center'}}>
            {loader ? (
                <div style={{display: 'flex', alignItems: 'center', justifyContent: 'center'}}>
                    <Loader/>
                </div>
            ) : (
                <table style={{width: '100%', margin: 'auto'}}>
                    <thead>
                    <tr>
                        <th>Name</th>
                        <th>Birth Year</th>
                        <th>Group name</th>
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
            )}
        </div>
    );
};

export default Siblings;