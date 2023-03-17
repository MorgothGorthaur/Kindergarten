import {useState, useEffect} from "react";
import {Button, Modal, ModalFooter} from "react-bootstrap";
import RelativesService from "../API/RelativesService";
import RelativeListItem from "./RelativeLIstItem";
import RelativeForm from "./RelativeForm";
import Loader from "../UI/Loader/Loader";
import React from "react";

function Relatives({tokens, setTokens, kidId}) {
    const [relatives, setRelatives] = useState([]);
    const [showForm, setShowForm] = useState(false);
    const [loader, setLoader] = useState(true);
    useEffect(() => {
        async function fetchRelatives() {
            RelativesService.get(tokens, setTokens, kidId).then((data) => {
                setRelatives(data);
                setLoader(false);
            });
        }

        fetchRelatives();
    }, []);

    return (
        <div>
            {loader ? (
                <div style={{display: 'flex', alignItems: 'center', justifyContent: 'center'}}>
                    <Loader/>
                </div>
            ):(
                <div>
                    {relatives.length === 0 ? (
                        <p>Relatives not found.</p>
                    ) : (
                        <div className="relatives-list-container">
                            <h3>All Relatives</h3>
                            <ul className="relatives-list">
                                {relatives.map((relative) => (
                                    <RelativeListItem
                                        key={relative.id}
                                        relative={relative}
                                        tokens={tokens}
                                        setTokens={setTokens}
                                        relatives={relatives}
                                        setRelatives={setRelatives}
                                        kidId={kidId}
                                    />
                                ))}
                            </ul>
                        </div>
                    )}
                    <div className="add-relative-btn">
                        <Button variant="primary" onClick={() => setShowForm(true)}>Add Relative</Button>
                        <Modal show={showForm} onHide={setShowForm}>
                            <RelativeForm tokens={tokens} setTokens={setTokens} relatives={relatives}
                                          setRelatives={setRelatives} setShowForm={setShowForm} kidId={kidId}/>
                        </Modal>
                    </div>
                </div>
            )}
        </div>
    );
}

export default Relatives;