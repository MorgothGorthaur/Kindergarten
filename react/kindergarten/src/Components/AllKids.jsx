import {useEffect, useState} from "react";
import KidsService from "../API/KidsService";
import {Button, Modal} from "react-bootstrap";
import ChildForm from "./ChildForm";
import KidBrothersAndSisters from "./KidBrothersAndSisters";
import KidListItem from "./KidListItem";

function AllKids({tokens, setTokens}) {
    const [kids, setKids] = useState([]);


    useEffect(() => {
        async function fetchData() {
            const data = await KidsService.getAll(tokens, setTokens);
            setKids(data);
        }

        fetchData();
    }, []);


    return (
        <div>
            {kids.length === 0 ? (
                <p>Kids not found</p>
            ) : (
                <div className="kids-list-container">
                    <h3>All kids</h3>
                    <ul className="kids-list">
                        {kids.map((kid) => (
                            <KidListItem kid = {kid} kids={kids} setKids={setKids} tokens={tokens} setTokens={setTokens} />
                        ))}
                    </ul>
                </div>
            )}
        </div>
    );
}

export default AllKids;