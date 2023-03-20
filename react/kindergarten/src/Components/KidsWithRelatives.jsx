import {useState, useEffect} from 'react';
import KidsService from '../API/KidsService';
import Loader from "../UI/Loader/Loader";

function KidsWithRelatives({tokens, setTokens}) {
    const [kids, setKids] = useState([]);
    const [loader, setLoader] = useState(true);
    useEffect(() => {
        const fetchData = async () => {
            const data = await KidsService.getKidsWithRelatives(tokens, setTokens);
            setKids(data);
            setLoader(false);
        };
        fetchData();
    }, []);

    return (
        <div>
            <h3>Kids with relatives</h3>
            {loader ? (
                <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'center', height: '100vh' }}>
                    <Loader />
                </div>
            ) : kids.length > 0 ? (
                <table>
                    <thead>
                    <tr>
                        <th>Name</th>
                        <th>Birth year</th>
                        <th>Relatives</th>
                    </tr>
                    </thead>
                    <tbody>
                    {kids.map((kid) => (
                        <tr key={kid.id}>
                            <td>{kid.name}</td>
                            <td>{kid.birthYear}</td>
                            <td>
                                {kid.relatives.length > 0 ? (
                                    <table>
                                        <thead>
                                        <tr>
                                            <th>Name</th>
                                            <th>Address</th>
                                            <th>Phone</th>
                                        </tr>
                                        </thead>
                                        <tbody>
                                        {kid.relatives.map((relative) => (
                                            <tr key={relative.id}>
                                                <td>{relative.name}</td>
                                                <td>{relative.address}</td>
                                                <td>{relative.phone}</td>
                                            </tr>
                                        ))}
                                        </tbody>
                                    </table>
                                ) : (
                                    <h3 style={{textAlign: 'center'}}>No relatives found</h3>

                                )}
                            </td>
                        </tr>
                    ))}
                    </tbody>
                </table>
            ) : (
                <h3 style={{textAlign: 'center'}}>No kids found</h3>
            )}
        </div>
    );
}

export default KidsWithRelatives;